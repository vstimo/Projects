from model.Animal import Animal
from model.repository.AnimalRepository import AnimalRepository


def testAddNewAnimal():
    animalRepository = AnimalRepository("zootest")
    animal = Animal(10,"Bety","Guinea Pig","Herbivore","Savana")
    
    status1 = animalRepository.addAnimal(animal)
    status2 = animalRepository.addAnimal(animal)
    
    assert status1 == True
    assert status2 == False
    
def testFindAnimal():
    animalRepository = AnimalRepository("zootest")
    results1 = animalRepository.findBy("id", 1)
    results2 = animalRepository.findBy("id", 14)
    
    assert results1 != None
    assert len(results1) == 1
    assert results2 == None
    assert results1[0].name == "Simba"
    
def testFindAnimals():
    animalRepository = AnimalRepository("zootest")
    results = animalRepository.findBy("habitat", "savana")
    
    assert results != None
    assert len(results) == 2
    assert results[0].name == "Simba"
    assert results[1].name == "Longneck"
    
def testFindAllAnimals():
    animalRepository = AnimalRepository("zootest")
    results = animalRepository.findAll()
    
    assert results != None
    assert len(results) == 4
    assert results[0].name == "Simba"
    assert results[1].name == "Dumbo"
    assert results[2].name == "Longneck"
    assert results[3].name == "Stripes"
    
def testUpdateAnimal():
    animalRepository = AnimalRepository("zootest")
    
    result = animalRepository.updateAnimal(1,"name","Bety")
    
    assert result == True
    
def testDeleteAnimal():
    animalRepository = AnimalRepository("zootest")
    
    results1 = animalRepository.deleteAnimal("2")
    results2 = animalRepository.deleteAnimal("2")
    
    assert results1 == True
    assert results2 == False