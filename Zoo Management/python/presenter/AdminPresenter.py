from model.User import User
from model.repository.UserRepository import UserRepository

class AdminPresenter:
    def __init__(self, adminGUI):
        self.adminGUI = adminGUI
        self.userRepository = UserRepository()
        
    def addUser(self):
        id  = self.adminGUI.getIdEntry()
        username = self.adminGUI.getUsernameEntry()
        password = self.adminGUI.getPasswordEntry()
        admin = self.adminGUI.getAdminCheckBox()
        
        if(id != "" and username !="" and password != ""):
            existaUser1 = self.userRepository.findBy("id",id)
            existaUser = self.userRepository.findBy("username",username)
            if(existaUser1 != None):
                self.adminGUI.setErrorMessage("Error", f"User with id {id} already exists!")
            elif(existaUser != None):
                self.adminGUI.setErrorMessage("Error", f"User with id {username} already exists!")
            else:
                user = User(id, username, password, admin)
                success = self.userRepository.addUser(user)
                if(success == True):
                    self.adminGUI.setInfoMessage("Success", f"User {username} was successfully added to database!")
                else: self.adminGUI.setErrorMessage("Error", f"The user {username} couldn't be added to database!")
        else:  self.adminGUI.setErrorMessage("Error", "Please fill all the fields!")
    
    def findUsersBy(self):
        category = self.adminGUI.getSearchCriteriaCombo()
        filter = self.adminGUI.getCriteriaEntry()
        
        if(filter != ""):
            if(category == "Admin"):
                if filter=="True": filter="1"
                elif filter =="False": filter="0"
                else:
                    self.adminGUI.setErrorMessage("Error","Only True\False in the text field")    
            users=self.userRepository.findBy(category.lower(),filter.lower())
            if(users==None):
                self.adminGUI.setErrorMessage("Error","There are no users by this criteria!")
            else: 
                for i, user in enumerate(users, start=1):
                    label_text = None
                    if user.admin == 1:
                        label_text = f"{i}. User {user.username} with ID:{user.id} is an admin"
                    else: label_text = f"{i}. User {user.username} with ID:{user.id} is a worker"
                    self.adminGUI.setShowFrame(label_text)
        else: self.adminGUI.setErrorMessage("Error","Please name a filter for the specified category!")
    
    def findAllUsers(self, frame):
        users = self.userRepository.findAll()
        usersNames = []
        for user in users:
            usersNames.append(f"({user.id}) {user.username}")
        self.adminGUI.setSearchUserCombo(usersNames, frame)
    
    def updateUser(self):
        user = self.adminGUI.getSearchUserCombo()
        userParts = user.split()
        userID = userParts[0][1:-1]
        userName = ' '.join(userParts[1:])
        
        category = self.adminGUI.getSearchCriteriaCombo()
        newValue = self.adminGUI.getCriteriaEntry()
        adminStatus = self.adminGUI.getAdminChoice()
        
        if(filter != ""):
            success = self.userRepository.updateUser(userID, category.lower(), newValue)
            success2 = self.userRepository.updateUser(userID, "admin", adminStatus)
            if success == None or success2 == None:
                self.adminGUI.setErrorMessage("Error","The user couldn't be updated!")
            else: self.adminGUI.setInfoMessage("Success", f"User {userName} was successfully updated!")
        else: self.adminGUI.setErrorMessage("Error","Please give a new value for the selected user!")
    
    def deleteUser(self):
        user = self.adminGUI.getSearchUserCombo()
        userParts = user.split()
        userID = userParts[0][1:-1]
        userName = ' '.join(userParts[1:])
        
        success = self.userRepository.deleteUser(userID)
        if success == None:
            self.adminGUI.setErrorMessage("Error","The user couldn't be deleted!")
        else: self.adminGUI.setInfoMessage("Success", f"User {userName} was successfully deleted!")
        
    def printAllUsers(self):
        users = self.userRepository.findAll()
        for i, user in enumerate(users, start=1):
            label_text = None
            if user.admin == 1:
                label_text = f"{i}. User {user.username} with ID:{user.id} is an admin"
            else: label_text = f"{i}. User {user.username} with ID:{user.id} is a worker"
            self.adminGUI.setShowFrame(label_text)
                    
    def resetFrame(self, showFrameC):
        for widget in showFrameC:
            widget.destroy()