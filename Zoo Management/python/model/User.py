class User:
    def __init__(self, id, username, password, admin):
        self.id = id
        self.username = username
        self.password = password
        self.admin = admin
        
    def getID(self):
        return self.id
    
    def getUsername(self):
        return self.username
    
    def getPassword(self):
        return self.password
    
    def getAdmin(self):
        return self.admin