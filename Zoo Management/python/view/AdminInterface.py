import abc

from view.UserInterface import UserInterface

class AdminInterface(UserInterface):
    @abc.abstractmethod
    def getIdEntry(self):
        pass
    
    @abc.abstractmethod
    def getUsernameEntry(self):
        pass
    
    @abc.abstractmethod
    def getPasswordEntry(self):
        pass
    
    @abc.abstractmethod
    def getAdminCheckBox(self):
        pass
    
    @abc.abstractmethod
    def setInfoMessage(self, titlu, message):
        pass
    
    @abc.abstractmethod
    def getSearchCriteriaCombo(self):
        pass
    
    @abc.abstractmethod
    def getCriteriaEntry(self):
        pass
    
    @abc.abstractmethod
    def getSearchUserCombo(self):
        pass
    
    @abc.abstractmethod
    def setShowFrame(self, label_text):
        pass
        
    @abc.abstractmethod
    def setSearchUserCombo(self, values, frame1):
       pass
        
    @abc.abstractmethod    
    def getAdminChoice(self):
        pass