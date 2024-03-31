from model.Animal import Animal
from model.repository.AnimalRepository import AnimalRepository

class WorkerPresenter:
    def __init__(self, workerGUI):
        self.workerGUI = workerGUI
        self.animalRepository = AnimalRepository()
        
    def addAnimal(self):
        id = self.workerGUI.getIdEntry()
        name = self.workerGUI.getNameEntry()
        species= self.workerGUI.getSpeciesEntry()
        diet = self.workerGUI.getDietEntry()
        habitat = self.workerGUI.getHabitatEntry()
        
        if(id != "" and name !="" and species != "" and diet != "" and habitat != ""):
            existaAnimal = self.animalRepository.findBy("id",id)
            if(existaAnimal != None):
                self.workerGUI.setErrorMessage("Error", f"Animal with id {id} already exists!")
            else:
                animal = Animal(id, name, species, diet, habitat)
                success = self.animalRepository.addAnimal(animal)
                if(success == True):
                    self.workerGUI.setInfoMessage("Success", f"Animal {name} was successfully added to database!")
                else: self.workerGUI.setErrorMessage("Error", f"The animal {name} couldn't be added to database!")
        else:  self.workerGUI.setErrorMessage("Error", "Please fill all the fields!")
    
    def findAnimalsBy(self):
        category = self.workerGUI.getSearchCriteriaCombo()
        filter = self.workerGUI.getCriteriaEntry()
        
        if(filter != ""):
            animals=self.animalRepository.findBy(category.lower(),filter.lower())
            if(animals==None):
                self.workerGUI.setErrorMessage("Error","There are no animals by this criteria!")
            else: 
                for i, animal in enumerate(animals, start=1):
                    label_text = f"{i}. {animal.name} is a {animal.species} ({animal.diet}) - Habitat: {animal.habitat}"
                    self.workerGUI.setShowFrame(label_text)
        else: self.workerGUI.setErrorMessage("Error","Please name a filter for the specified category!")
        
    def findAllAnimals(self, frame1):
        animals = self.animalRepository.findAll()
        animalNames = []
        for animal in animals:
            animalNames.append(f"({animal.id}) {animal.name}" )
        self.workerGUI.setSearchAnimalCombo(animalNames, frame1)
    
    def updateAnimal(self):
        animal = self.workerGUI.getSearchAnimalCombo()
        animalParts = animal.split()
        animalID = animalParts[0][1:-1]
        animalName = ' '.join(animalParts[1:])
        
        category = self.workerGUI.getSearchCriteriaCombo()
        newValue = self.workerGUI.getCriteriaEntry()
        if(filter != ""):
            success = self.animalRepository.updateAnimal(animalID, category.lower(), newValue)
            if success == None:
                self.workerGUI.setErrorMessage("Error","The animal couldn't be updated!")
            else: self.workerGUI.setInfoMessage("Success", f"Animal {animalName} was successfully updated!")
        else: self.workerGUI.setErrorMessage("Error","Please give a new value for the selected animal!")
        
    def deleteAnimal(self):
        animal = self.workerGUI.getSearchAnimalCombo()
        animalParts = animal.split()
        animalID = animalParts[0][1:-1]
        animalName = ' '.join(animalParts[1:])
        
        success = self.animalRepository.deleteAnimal(animalID)
        if success == None:
            self.workerGUI.setErrorMessage("Error","The animal couldn't be deleted!")
        else: self.workerGUI.setInfoMessage("Success", f"Animal {animalName} was successfully deleted!")
        
    def resetFrame(self, showFrameC):
        for widget in showFrameC:
            widget.destroy()