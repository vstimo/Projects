from model.repository.UserRepository import UserRepository

class LoginPresenter:
    def __init__(self, loginGUI):
        self.loginGUI=loginGUI
        self.userRepository=UserRepository()
    
    def findUserBy(self):
        username=self.loginGUI.getUsernameText()
        password=self.loginGUI.getPasswordText()
        if(username != "" and password != ""):
            users = self.userRepository.findBy("username", username)
            user = users.pop()
            if(user==None): self.loginGUI.setErrorMessage("Error", "The user was not found!")
            elif(user.password!=password): self.loginGUI.setErrorMessage("Error", "Incorrect password!")
            else: 
                self.loginGUI.setRootDisappear()
                if(user.admin == True): self.loginGUI.turnOnAdminGUI(user.username)
                elif(user.admin == False): self.loginGUI.turnOnWorkerGUI(user.username)
        else: self.loginGUI.setErrorMessage("Error", "Please fill all the fields!")