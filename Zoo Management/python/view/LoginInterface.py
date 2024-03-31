import abc

from view.UserInterface import UserInterface

class LoginInterface(UserInterface):
    @abc.abstractmethod
    def getUsernameText(self): pass
    
    @abc.abstractmethod
    def getPasswordText(self): pass
    
    @abc.abstractmethod
    def getRoot(self): pass
    
    @abc.abstractmethod
    def turnOnWorkerGUI(self, username): pass 
    
    @abc.abstractmethod
    def turnOnAdminGUI(self, username): pass