import abc

from view.UserInterface import UserInterface

class WorkerInterface(UserInterface):
    @abc.abstractmethod
    def getIdEntry(self):
        pass
    
    @abc.abstractmethod
    def getNameEntry(self):
        pass
    
    @abc.abstractmethod
    def getSpeciesEntry(self):
        pass
    
    @abc.abstractmethod
    def getDietEntry(self):
        pass
    
    @abc.abstractmethod
    def getHabitatEntry(self):
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
    def getSearchAnimalCombo(self):
        pass
    
    @abc.abstractmethod
    def setShowFrame(self, label_text):
        pass