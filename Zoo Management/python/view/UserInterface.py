import abc

class UserInterface(abc.ABC):
    @abc.abstractmethod
    def setErrorMessage(self, titlu, message): pass