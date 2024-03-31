class Animal:
    def __init__(self, id, name, species, diet, habitat):
        self.id = id
        self.name = name
        self.species = species
        self.diet = diet
        self.habitat = habitat

    def getID(self): return self.id
    
    def getName(self): return self.name
    
    def getSpecies(self): return self.species
    
    def getDiet(self): return self.diet
    
    def getHabitat(self): return self.habitat