#version 410 core
//TIMO

in vec3 fPosition;
in vec3 fNormal;
in vec2 fragTexCoords;
in vec4 fragPosLightSpace;

out vec4 fColor;

//matrices
uniform mat4 model;
uniform mat4 view;
uniform mat3 normalMatrix;

//lighting
uniform	vec3 lightDir;
uniform	vec3 lightColor;
uniform vec3 lightPunctiform;
uniform vec3 lightPunctiformColor;

//textures
uniform sampler2D diffuseTexture;
uniform sampler2D specularTexture;
uniform sampler2D shadowMap;

//components
vec3 ambient;
float ambientStrength = 0.2f;
vec3 diffuse;
vec3 specular;
float specularStrength = 0.5f;
float shininess = 32.0f;

float constant = 1.0f;
float linear = 0.22f, linearMare = 0.00091f;
float quadratic = 0.20f, quadraticMare = 0.00000455f;

vec4 fPosEye;

uniform float fogDensity;

void computeDirectLight(){
	fPosEye = view * model * vec4(fPosition, 1.0f);

	//transform normal
	vec3 normalEye = normalize(normalMatrix * fNormal);

	//normalize light direction
    	vec3 lightDirN = vec3(normalize(view * vec4(lightDir, 0.0f)));

	//compute view direction
	vec3 viewDirN = normalize(- fPosEye.xyz);
	
	//compute ambient light
	ambient = ambientStrength * lightColor;
	
	//compute diffuse light
	diffuse = max(dot(normalEye, lightDirN), 0.0f) * lightColor;

	//compute specular light
	vec3 reflectDir = reflect(-lightDirN, normalEye);
	float specCoeff = pow(max(dot(viewDirN, reflectDir), 0.0f), shininess);
	specular = specularStrength * specCoeff * lightColor;
}

vec3 computePunctiformLight(vec3 lightPunctiform, vec3 lightPunctiformColor, vec3 diffTex, vec3 specTex, float constant, float linear, float quadratic) {
	fPosEye = vec4(fPosition, 1.0f);

	//compute distance to light
	float dist = length(lightPunctiform - fPosEye.xyz);
	//compute attenuation
	float att = 1.0f / (constant + linear * dist + quadratic * (dist * dist));

	 //transform normal
	vec3 normalEye = normalize(fNormal);
    	//compute light direction
	vec3 lightDirN = normalize(lightPunctiform -  fPosEye.xyz);
    	//compute view direction 
	vec3 viewDirN = normalize(lightPunctiform- fPosEye.xyz);

	//compute ambient light
	vec3 ambient1 = att  *ambientStrength * lightPunctiformColor;
    	//compute diffuse light
	vec3 diffuse1 = att * max(dot(normalEye, lightDirN), 0.0f) * lightPunctiformColor;

	vec3 halfVector = normalize(lightDirN + viewDirN);
	float specCoeff = pow(max(dot(viewDirN, halfVector), 0.3f), 50.0f);
	vec3 specular1 = att * specularStrength * specCoeff * lightPunctiformColor;

	return  min(((ambient + diffuse) * diffTex + specular * specTex ) * att * 2, 1.0f);
}

float computeShadow()
{
	vec3 normalizedCoords = fragPosLightSpace.xyz / fragPosLightSpace.w;

	normalizedCoords = normalizedCoords * 0.5 + 0.5;
	
	if (normalizedCoords.z > 1.0f) return 0.0f;

	float closestDepth = texture(shadowMap, normalizedCoords.xy).r;

	float currentDepth = normalizedCoords.z;

	float bias = max(0.05f * (1.0f - dot(fNormal,lightDir)), 0.05f);
	float shadow = currentDepth - bias > closestDepth ? 1.0 : 0.0;	

	return shadow;
}

float computeFog() { 
    fPosEye = vec4(fPosition, 1.0f);
    float fragmentDistance = length(fPosEye); 
    float fogFactor = exp(-pow(fragmentDistance * fogDensity, 2)); 

    return clamp(fogFactor, 0.0f, 1.0f); 
}

void main() 
{
	vec4 texColorDiffuse = texture(diffuseTexture, fragTexCoords);
    	if(texColorDiffuse.a < 0.1 ) discard;

    	vec4 texColorSpecular = texture(specularTexture, fragTexCoords);
    	if(texColorSpecular.a < 0.1 ) discard;

	computeDirectLight();
	
	vec3 luminiCasa = vec3(1.0f, 1.0f, 0.0f);

	vec3 punctLight1_1 = computePunctiformLight(vec3(-206.3f, 2.78f, -363.91f), luminiCasa, texColorDiffuse.rgb, texColorSpecular.rgb, constant, linear, quadratic);
	vec3 punctLight1_2 = computePunctiformLight(vec3(-207.50f, 2.72f, -364.55f), luminiCasa, texColorDiffuse.rgb, texColorSpecular.rgb, constant, linear, quadratic);
	vec3 punctLight1_3 = computePunctiformLight(vec3(-207.50f, 2.63f, -363.17f), luminiCasa, texColorDiffuse.rgb, texColorSpecular.rgb, constant, linear, quadratic);

	vec3 punctLight2_1 = computePunctiformLight(vec3(-223.08f, 3.5f, -355.44f), luminiCasa, texColorDiffuse.rgb, texColorSpecular.rgb, constant, linear, quadratic);
	vec3 punctLight2_2 = computePunctiformLight(vec3(-224.02f, 3.67f, -354.93f), luminiCasa, texColorDiffuse.rgb, texColorSpecular.rgb, constant, linear, quadratic);
	vec3 punctLight2_3 = computePunctiformLight(vec3(-223.93f, 3.64f, -355.72f), luminiCasa, texColorDiffuse.rgb, texColorSpecular.rgb, constant, linear, quadratic);
	
	vec3 luminaLuna= computePunctiformLight(vec3(-1725.52f, 959.0f, -574.851f), luminiCasa, texColorDiffuse.rgb, texColorSpecular.rgb, constant, linearMare, quadraticMare);

	ambient = ambient * texColorDiffuse.rgb;
	diffuse *= texColorDiffuse.rgb;
	specular *= texColorSpecular.rgb;

	float shadow = computeShadow();
	vec3 color = min((ambient + (1.0f - shadow)*diffuse) + (1.0f - shadow)*specular, 1.0f);
	
	color = color + punctLight1_1 + punctLight1_2 + punctLight1_3 + punctLight2_1 + punctLight2_2 + punctLight2_3 + luminaLuna;
	
	float fogFactor = computeFog();
	vec4 fogColor = vec4(0.5f, 0.5f, 0.5f, 1.0f);
	fColor = mix(fogColor, vec4(color, 1.0f), fogFactor);
}
