import abc
from view.UserInterface import UserInterface

class GuestInterface(UserInterface):
    @abc.abstractmethod
    def getSelectedCategory(self):
        pass
    
    @abc.abstractmethod
    def getFilterEntry(self):
        pass
    
    @abc.abstractmethod
    def setShowFrame(self, label_text):
        pass
    
    @abc.abstractmethod
    def setRootAsNewWindow(self):
        pass