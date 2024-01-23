#if defined (__APPLE__)
#define GLFW_INCLUDE_GLCOREARB
#define GL_SILENCE_DEPRECATION
#else
#define GLEW_STATIC
#include "GL/glew.h"
#endif

#include "GLFW/glfw3.h"

#include "glm/glm.hpp" //core glm functionality
#include "glm/gtc/matrix_transform.hpp" //glm extension for generating common transformation matrices
#include "glm/gtc/matrix_inverse.hpp" //glm extension for computing inverse matrices
#include "glm/gtc/type_ptr.hpp "//glm extension for accessing the internal data structure of glm types

#include "Window.h"
#include "Shader.hpp"
#include "Camera.hpp"
#include "Model3D.hpp"
#include "SkyBox.hpp"

#include <iostream>

// window
gps::Window myWindow;
int WINDOW_WIDTH = 1024, WINDOW_HEIGHT = 768;
int retina_width, retina_height;

// matrices
glm::mat4 model;
glm::mat4 view;
glm::mat4 projection;
glm::mat3 normalMatrix;
glm::mat4 lightRotation;

// light parameters
glm::vec3 lightDir;
glm::vec3 lightColor;

// shader uniform locations
GLuint modelLoc;
GLuint viewLoc;
GLuint projectionLoc;
GLuint normalMatrixLoc;
GLint lightDirLoc;
GLint lightColorLoc;

// camera
gps::Camera myCamera(
	glm::vec3(0.0f, 10.0f, 3.0f),
	glm::vec3(0.0f, 0.0f, 0.0f),
	glm::vec3(0.0f, 1.0f, 0.0f));

GLfloat cameraSpeed = 0.1f;
GLfloat cameraAnimationSpeed = 2.0f;
GLboolean pressedKeys[1024];

bool firstMouse = true;
float lastX = WINDOW_WIDTH / 2;
float lastY = WINDOW_HEIGHT / 2;
float yaw = -90.0f, pitch = 0.0f;
const float sensitivity = 0.3f;

// models
gps::Model3D islands;
gps::Model3D boat;
gps::Model3D sea;
gps::Model3D sun;
gps::Model3D screenQuad;

// shaders
gps::Shader myBasicShader;
gps::Shader depthShader;
gps::Shader lightShader;
gps::Shader screenQuadShader;
gps::Shader skyboxShader;

//shadow
const unsigned int SHADOW_WIDTH = 2048*8;
const unsigned int SHADOW_HEIGHT = 2048*8;
bool showDepthMap;

GLuint shadowMapFBO;
GLuint depthMapTexture;

//skybox
gps::SkyBox mySkyBox;

//fog
GLfloat fogDensity = 0.000f;
GLfloat fogDensityLoc;
GLfloat fogIncrease = 0.00005f;

//animation
bool automove = false;
int nr;

GLenum glCheckError_(const char* file, int line)
{
	GLenum errorCode;
	while ((errorCode = glGetError()) != GL_NO_ERROR) {
		std::string error;
		switch (errorCode) {
		case GL_INVALID_ENUM:
			error = "INVALID_ENUM";
			break;
		case GL_INVALID_VALUE:
			error = "INVALID_VALUE";
			break;
		case GL_INVALID_OPERATION:
			error = "INVALID_OPERATION";
			break;
		case GL_OUT_OF_MEMORY:
			error = "OUT_OF_MEMORY";
			break;
		case GL_INVALID_FRAMEBUFFER_OPERATION:
			error = "INVALID_FRAMEBUFFER_OPERATION";
			break;
		}
		std::cout << error << " | " << file << " (" << line << ")" << std::endl;
	}
	return errorCode;
}
#define glCheckError() glCheckError_(__FILE__, __LINE__)

void windowResizeCallback(GLFWwindow* window, int width, int height) {
	fprintf(stdout, "Window resized! New width: %d, and height: %d\n", width, height);

	glfwGetFramebufferSize(window, &retina_width, &retina_height);
	WindowDimensions windowDimensions = { retina_width, retina_height };
	myWindow.setWindowDimensions(windowDimensions);
	glViewport(0, 0, retina_width, retina_height);

	myBasicShader.useShaderProgram();
	float aspectRatio = (float)width / (float)height;
	projection = glm::perspective(glm::radians(45.0f), aspectRatio, 0.1f, 10000.0f);
	projectionLoc = glGetUniformLocation(myBasicShader.shaderProgram, "projection");
	glUniformMatrix4fv(projectionLoc, 1, GL_FALSE, glm::value_ptr(projection));
}

void keyboardCallback(GLFWwindow* window, int key, int scancode, int action, int mode) {
	if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
		glfwSetWindowShouldClose(window, GL_TRUE);

	if (key == GLFW_KEY_M && action == GLFW_PRESS) showDepthMap = !showDepthMap;

	if (key == GLFW_KEY_R && action == GLFW_PRESS) automove = true;

	if (pressedKeys[GLFW_KEY_Z]) {glPolygonMode(GL_FRONT_AND_BACK, GL_FILL); glDisable(GL_LINE_SMOOTH); } //se vede tot

	if (pressedKeys[GLFW_KEY_X]) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE); //se vad doar liniile

	if (pressedKeys[GLFW_KEY_C]) glPolygonMode(GL_FRONT_AND_BACK, GL_POINT); //se vad doar punctele

	if (pressedKeys[GLFW_KEY_V]) { glPolygonMode(GL_FRONT_AND_BACK, GL_FILL); glEnable(GL_LINE_SMOOTH); } //se vede tot smooth

	if (key >= 0 && key < 1024) {
		if (action == GLFW_PRESS) {
			pressedKeys[key] = true;
		}
		else if (action == GLFW_RELEASE) {
			pressedKeys[key] = false;
		}
	}
}

void mouseCallback(GLFWwindow* window, double xpos, double ypos) {
	if (firstMouse) {
		lastX = xpos;
		lastY = ypos;
		firstMouse = false;
	}

	float xoffset = xpos - lastX;
	float yoffset = lastY - ypos;
	lastX = xpos;
	lastY = ypos;


	xoffset *= sensitivity;
	yoffset *= sensitivity;

	yaw += xoffset;
	pitch += yoffset;

	if (pitch > 89.0f) pitch = 89.0f;
	if (pitch < -89.0f) pitch = -89.0f;

	myCamera.rotate(pitch, yaw);
	view = myCamera.getViewMatrix();
	myBasicShader.useShaderProgram();
	glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
}

float angle;
GLfloat lightAngle;
void processMovement() {
	if (pressedKeys[GLFW_KEY_W]) {
		automove = false;
		myCamera.move(gps::MOVE_FORWARD, cameraSpeed);
		view = myCamera.getViewMatrix();
		myBasicShader.useShaderProgram();
		glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
		normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
	}

	if (pressedKeys[GLFW_KEY_S]) {
		automove = false;
		myCamera.move(gps::MOVE_BACKWARD, cameraSpeed);
		view = myCamera.getViewMatrix();
		myBasicShader.useShaderProgram();
		glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
		normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
	}

	if (pressedKeys[GLFW_KEY_A]) {
		automove = false;
		myCamera.move(gps::MOVE_LEFT, cameraSpeed);
		view = myCamera.getViewMatrix();
		myBasicShader.useShaderProgram();
		glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
		normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
	}

	if (pressedKeys[GLFW_KEY_D]) {
		automove = false;
		myCamera.move(gps::MOVE_RIGHT, cameraSpeed);
		view = myCamera.getViewMatrix();
		myBasicShader.useShaderProgram();
		glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
		normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
	}

	if (pressedKeys[GLFW_KEY_Q]) { angle -= 0.02f; automove = false;}

	if (pressedKeys[GLFW_KEY_E]) { angle += 0.02f; automove = false;}

	if (pressedKeys[GLFW_KEY_LEFT_SHIFT]) { cameraSpeed = 3.0f;}
	else { cameraSpeed = 0.2f;}

	if (pressedKeys[GLFW_KEY_J]) { lightAngle -= 0.2f; automove = false;}

	if (pressedKeys[GLFW_KEY_L]) { lightAngle += 0.2f; automove = false;}

	if (pressedKeys[GLFW_KEY_8]) {
		automove = false;
		fogDensity += fogIncrease;

		if (fogDensity > 0.01f || fogDensity < 0.0f)
			fogIncrease = -fogIncrease;

		myBasicShader.useShaderProgram();
		glUniform1f(fogDensityLoc, fogDensity);
	}
}

void animation() {
	if (automove) {
		if (nr < 170) myCamera.rotate(0.0f, 1.5f * nr);
		if (nr >= 170 && nr < 210) myCamera.move(gps::MOVE_FORWARD, cameraAnimationSpeed*4);
		if (nr >= 210 && nr < 230) angle -= 0.01f;
		if (nr >= 230 && nr < 250) myCamera.move(gps::MOVE_FORWARD, cameraAnimationSpeed);
		if (nr >= 250 && nr < 300) {angle -= 0.02f; myCamera.move(gps::MOVE_BACKWARD, cameraAnimationSpeed); }
		if (nr >= 300 && nr < 375) myCamera.move(gps::MOVE_BACKWARD, cameraAnimationSpeed*2);
		if (nr >= 375 && nr < 400) myCamera.move(gps::MOVE_LEFT, cameraAnimationSpeed);
		if (nr >= 400 && nr < 425) myCamera.move(gps::MOVE_RIGHT, cameraAnimationSpeed * 2);
		if (nr >= 425 && nr < 450) myCamera.move(gps::MOVE_LEFT, cameraAnimationSpeed);
		if (nr >= 450 && nr < 650) angle += 0.02f;
		nr++;
	}
}

void initOpenGLWindow() {
	myWindow.Create(WINDOW_WIDTH, WINDOW_HEIGHT, "OpenGL Project Core");
	glfwGetFramebufferSize(myWindow.getWindow(), &retina_width, &retina_height);
	WindowDimensions a = { retina_width, retina_height };
	myWindow.setWindowDimensions(a);
	glViewport(0, 0, (float)myWindow.getWindowDimensions().width, (float)myWindow.getWindowDimensions().height);
}

void setWindowCallbacks() {
	glfwSetWindowSizeCallback(myWindow.getWindow(), windowResizeCallback);
	glfwSetKeyCallback(myWindow.getWindow(), keyboardCallback);
	glfwSetCursorPosCallback(myWindow.getWindow(), mouseCallback);
	glfwSetInputMode(myWindow.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
}

void initOpenGLState() {
	glClearColor(0.7f, 0.7f, 0.7f, 1.0f);
	glViewport(0, 0, myWindow.getWindowDimensions().width, myWindow.getWindowDimensions().height);
	glEnable(GL_FRAMEBUFFER_SRGB);
	glEnable(GL_DEPTH_TEST);
	glDepthFunc(GL_LESS);
	glEnable(GL_CULL_FACE);
	glCullFace(GL_BACK);
	glFrontFace(GL_CCW);
	glEnable(GL_DEPTH_TEST);
}

void initModels() {
	islands.LoadModel("models/islands/islands.obj");
	sea.LoadModel("models/sea/sea.obj");
	boat.LoadModel("models/boat/boat.obj");
	sun.LoadModel("models/cube/moon.obj");
	screenQuad.LoadModel("models/quad/quad.obj");
}

void initSkyBox() {
	std::vector<const GLchar*> faces;
	faces.push_back("skybox/right.tga");
	faces.push_back("skybox/left.tga");
	faces.push_back("skybox/top.tga");
	faces.push_back("skybox/bottom.tga");
	faces.push_back("skybox/back.tga");
	faces.push_back("skybox/front.tga");
	mySkyBox.Load(faces);
}

void initShaders() {
	myBasicShader.loadShader("shaders/basic.vert", "shaders/basic.frag");
	lightShader.loadShader("shaders/lightCube.vert", "shaders/lightCube.frag");
	screenQuadShader.loadShader("shaders/screenQuad.vert", "shaders/screenQuad.frag");
	depthShader.loadShader("shaders/depth.vert", "shaders/depth.frag");
	skyboxShader.loadShader("shaders/skyboxShader.vert", "shaders/skyboxShader.frag");
}

glm::vec3 lightPosEye;
GLuint lightPosEyeLoc;
void initUniforms() {
	lightPosEye = glm::vec3(0, 0, 3.0f);
	lightPosEyeLoc = glGetUniformLocation(myBasicShader.shaderProgram, "lightPosEye");
	glUniform3fv(lightPosEyeLoc, 1, glm::value_ptr(lightPosEye));
	myBasicShader.useShaderProgram();

	model = glm::rotate(glm::mat4(1.0f), glm::radians(angle), glm::vec3(0.0f, 1.0f, 0.0f));
	modelLoc = glGetUniformLocation(myBasicShader.shaderProgram, "model");

	view = myCamera.getViewMatrix();
	viewLoc = glGetUniformLocation(myBasicShader.shaderProgram, "view");
	glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));

	normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
	normalMatrixLoc = glGetUniformLocation(myBasicShader.shaderProgram, "normalMatrix");

	projection = glm::perspective(glm::radians(45.0f),
		(float)myWindow.getWindowDimensions().width / (float)myWindow.getWindowDimensions().height,
		0.1f, 10000.0f);
	projectionLoc = glGetUniformLocation(myBasicShader.shaderProgram, "projection");
	glUniformMatrix4fv(projectionLoc, 1, GL_FALSE, glm::value_ptr(projection));

	lightDir = glm::vec3(0.0f, 1000.0f, -1900.0f);
	glm::mat4 startRotation = glm::rotate(glm::mat4(1.0f), glm::radians(71.6f), glm::vec3(0.0f, 1.0f, 0.0f));
	lightDir = glm::mat3(startRotation) * lightDir;
	lightDirLoc = glGetUniformLocation(myBasicShader.shaderProgram, "lightDir");
	glUniform3fv(lightDirLoc, 1, glm::value_ptr(lightDir));

	lightColor = glm::vec3(1.0f, 1.0f, 1.0f);
	lightColorLoc = glGetUniformLocation(myBasicShader.shaderProgram, "lightColor");
	glUniform3fv(lightColorLoc, 1, glm::value_ptr(lightColor));

	fogDensityLoc = glGetUniformLocation(myBasicShader.shaderProgram, "fogDensity");
	glUniform1f(fogDensityLoc, fogDensity);

	lightShader.useShaderProgram();
	glUniformMatrix4fv(glGetUniformLocation(lightShader.shaderProgram, "projection"), 1, GL_FALSE, glm::value_ptr(projection));
}

void initFBO() {
	glGenFramebuffers(1, &shadowMapFBO);

	glGenTextures(1, &depthMapTexture);
	glBindTexture(GL_TEXTURE_2D, depthMapTexture);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT,
		SHADOW_WIDTH, SHADOW_HEIGHT, 0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	float borderColor[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

	glBindFramebuffer(GL_FRAMEBUFFER, shadowMapFBO);
	glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMapTexture, 0);

	glDrawBuffer(GL_NONE);
	glReadBuffer(GL_NONE);
	glBindFramebuffer(GL_FRAMEBUFFER, 0);
}

glm::mat4 computeLightSpaceTrMatrix() {
	glm::mat4 lightView = glm::lookAt(glm::inverseTranspose(glm::mat3(lightRotation)) * lightDir, glm::vec3(0.0f), glm::vec3(0.0f, 1.0f, 0.0f));
	const GLfloat near_plane = 0.01f, far_plane = 2700.0f;
	glm::mat4 lightProjection = glm::ortho(-2600.0f, 2600.0f, -2600.0f, 2600.0f, near_plane, far_plane);
	glm::mat4 lightSpaceTrMatrix = lightProjection * lightView;
	return lightSpaceTrMatrix;
}

void renderIslands(gps::Shader shader) {
	shader.useShaderProgram();

	model = glm::rotate(glm::mat4(1.0f), angle, glm::vec3(0, 1, 0));
	modelLoc = glGetUniformLocation(shader.shaderProgram, "model");

	glUniformMatrix4fv(modelLoc, 1, GL_FALSE, glm::value_ptr(model));
	glUniformMatrix3fv(normalMatrixLoc, 1, GL_FALSE, glm::value_ptr(normalMatrix));

	islands.Draw(shader);
}

void renderSun(gps::Shader shader) {
	lightShader.useShaderProgram();

	glUniformMatrix4fv(glGetUniformLocation(lightShader.shaderProgram, "view"), 1, GL_FALSE, glm::value_ptr(view));

	model = lightRotation;
	model = glm::translate(model, 1.0f * lightDir);
	model = glm::scale(model, glm::vec3(50.0f, 50.0f, 50.0f));
	glUniformMatrix4fv(glGetUniformLocation(lightShader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(model));

	sun.Draw(lightShader);
}

void renderSea(gps::Shader shader) {
	shader.useShaderProgram();

	model = glm::translate(glm::mat4(1.0f), glm::vec3(0.0f, 0.0f, 0.0f));

	float seaMovementSpeed = 2.5f;
	float currentTime = glfwGetTime();
	float seaXPosition = sin(currentTime) * seaMovementSpeed;
	model = glm::rotate(glm::mat4(1.0f), angle, glm::vec3(0, 1, 0));
	model = glm::translate(model, glm::vec3(seaXPosition, 0.0f, 0.0f));


	glUniformMatrix4fv(modelLoc, 1, GL_FALSE, glm::value_ptr(model));
	normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
	glUniformMatrix3fv(normalMatrixLoc, 1, GL_FALSE, glm::value_ptr(normalMatrix));

	sea.Draw(shader);
}

float angleBoat = 0.0f;

// Update this function to include animation
void renderBoat(gps::Shader shader) {
	// Calculate the model matrix for rotation around (0,0,0)
	glm::mat4 model = glm::rotate(glm::mat4(1.0f), angleBoat, glm::vec3(0, 1, 0));
	model = glm::rotate(glm::mat4(1.0f), angle, glm::vec3(0, 1, 0));
	// Calculate the model matrix for rotation around the center of the boat
	glm::mat4 boatCenterRotation = glm::rotate(glm::mat4(1.0f), angleBoat, glm::vec3(0, 1, 0));

	// Combine the two rotations by translating the boat to the center, applying rotation, and then translating back
	glm::mat4 modelCentered = boatCenterRotation * glm::translate(glm::mat4(1.0f), -glm::vec3(3.05f, 0.0f, 0.18f)) * model;

	// Use the resulting model matrix in the shader
	glUniformMatrix4fv(modelLoc, 1, GL_FALSE, glm::value_ptr(modelCentered));

	// Increment the angle for the next frame
	angleBoat -= 0.001f;  // You can adjust the speed of rotation as needed

	// Rest of the rendering code...
	shader.useShaderProgram();
	glUniformMatrix3fv(normalMatrixLoc, 1, GL_FALSE, glm::value_ptr(normalMatrix));

	boat.Draw(shader);
}

void drawObjects(gps::Shader shader, bool depthPass) {
	shader.useShaderProgram();

	model = glm::rotate(glm::mat4(1.0f), glm::radians(angle), glm::vec3(0.0f, 1.0f, 0.0f));
	glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(model));

	// do not send the normal matrix if we are rendering in the depth map
	if (!depthPass) {
		normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
		glUniformMatrix3fv(normalMatrixLoc, 1, GL_FALSE, glm::value_ptr(normalMatrix));
	}

	renderIslands(shader);
	renderBoat(shader);

	model = glm::translate(glm::mat4(1.0f), glm::vec3(0.0f, -1.0f, 0.0f));
	glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(model));

	// do not send the normal matrix if we are rendering in the depth map
	if (!depthPass) {
		normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
		glUniformMatrix3fv(normalMatrixLoc, 1, GL_FALSE, glm::value_ptr(normalMatrix));
	}

	renderSea(shader);
}

void renderShadow() {
	depthShader.useShaderProgram();
	glUniformMatrix4fv(glGetUniformLocation(depthShader.shaderProgram, "lightSpaceTrMatrix"),
		1, GL_FALSE, glm::value_ptr(computeLightSpaceTrMatrix()));
	glViewport(0, 0, SHADOW_WIDTH, SHADOW_HEIGHT);
	glBindFramebuffer(GL_FRAMEBUFFER, shadowMapFBO);
	glClear(GL_DEPTH_BUFFER_BIT);
	drawObjects(depthShader, true);
	glBindFramebuffer(GL_FRAMEBUFFER, 0);
}

void renderScene() {
	renderShadow();

	if (showDepthMap) {
		glViewport(0, 0, retina_width, retina_height);

		glClear(GL_COLOR_BUFFER_BIT);

		screenQuadShader.useShaderProgram();

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, depthMapTexture);
		glUniform1i(glGetUniformLocation(screenQuadShader.shaderProgram, "depthMap"), 0);
		glDisable(GL_DEPTH_TEST);
		screenQuad.Draw(screenQuadShader);
		glEnable(GL_DEPTH_TEST);
	}
	else {
		glViewport(0, 0, retina_width, retina_height);

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		myBasicShader.useShaderProgram();

		view = myCamera.getViewMatrix();
		glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));

		lightRotation = glm::rotate(glm::mat4(1.0f), glm::radians(lightAngle), glm::vec3(0.0f, 1.0f, 0.0f));
		glUniform3fv(lightDirLoc, 1, glm::value_ptr(glm::inverseTranspose(glm::mat3(lightRotation)) * lightDir));

		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, depthMapTexture);
		glUniform1i(glGetUniformLocation(myBasicShader.shaderProgram, "shadowMap"), 3);
		
		glUniformMatrix4fv(glGetUniformLocation(myBasicShader.shaderProgram, "lightSpaceTrMatrix"),
			1, GL_FALSE, glm::value_ptr(computeLightSpaceTrMatrix()));

		drawObjects(myBasicShader, false);

		renderSun(lightShader);
	}

	skyboxShader.useShaderProgram();
	mySkyBox.Draw(skyboxShader, view, projection);
	//glm::vec3 cameraPosition = myCamera.getCameraPosition();
	//std::cout << "Camera Position: (" << cameraPosition.x << ", " << cameraPosition.y << ", " << cameraPosition.z << ")" << std::endl;
}

void cleanup() {
	glDeleteTextures(1, &depthMapTexture);
	glBindFramebuffer(GL_FRAMEBUFFER, 0);
	glDeleteFramebuffers(1, &shadowMapFBO);
	myWindow.Delete();
}

int main(int argc, const char* argv[]) {
	try {
		initOpenGLWindow();
	}
	catch (const std::exception& e) {
		std::cerr << e.what() << std::endl;
		return EXIT_FAILURE;
	}

	initOpenGLState();
	initModels();
	initShaders();
	initUniforms();
	initSkyBox();
	setWindowCallbacks();
	initFBO();

	//glCheckError();
	while (!glfwWindowShouldClose(myWindow.getWindow())) {
		processMovement();
		renderScene();
		animation();

		glfwPollEvents();
		glfwSwapBuffers(myWindow.getWindow());

		// glCheckError();
	}
	cleanup();

	return EXIT_SUCCESS;
}