from model.repository.AnimalRepository import AnimalRepository

class GuestPresenter:
    def __init__(self, guestGUI):
        self.guestGUI = guestGUI
        self.animalRepository = AnimalRepository()
        
    def findAllAnimalsSorted(self):
        animals = self.animalRepository.findAll()
        animals.sort(key=lambda x: (x.diet, x.species, x.id))
        if animals != []:   
            for i, animal in enumerate(animals, start=1):
                label_text = f"{i}. {animal.name} is a {animal.species} ({animal.diet}) - Habitat: {animal.habitat}"
                self.guestGUI.setShowFrame(label_text)
    
    def findAnimalsBy(self):
        category=self.guestGUI.getSelectedCategory()
        filter=self.guestGUI.getFilterEntry()
        if(filter != ""):
            animals=self.animalRepository.findBy(category.lower(),filter.lower())
            if(animals==None):
                self.guestGUI.setErrorMessage("Error","There are no animals by this criteria!")
            else: 
                for i, animal in enumerate(animals, start=1):
                    label_text = f"{i}. {animal.name} is a {animal.species} ({animal.diet}) - Habitat: {animal.habitat}"
                    self.guestGUI.setShowFrame(label_text)
        else: self.guestGUI.setErrorMessage("Error","Please name a filter for the specified category!")
        
    def resetFrame(self, showFrameC):
        for widget in showFrameC:
            widget.destroy()