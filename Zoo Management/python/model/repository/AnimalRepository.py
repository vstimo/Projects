import mysql.connector

from model.Animal import Animal
from model.repository.Connection import Connection

class AnimalRepository:
    def __init__(self, *args):
        if len(args)==1:    
            self.connection = Connection(args[0])
            self.connection.connectToDatabase()
            self.connection.createTable("animal")
        else: 
            self.connection = Connection()
            self.connection.connectToDatabase()
        
        
    def addAnimal(self, animal):
        try:
            instruction = f"INSERT INTO animal (id, name, species, diet, habitat) VALUES (%s, %s, %s, %s, %s);"
            values = (animal.getID(), animal.getName(), animal.getSpecies(), animal.getDiet(), animal.getHabitat())
            status=self.connection.executeSQL(instruction, values)
            if status==None: return False
            else: 
                print("Animal added successfully!")
                return True
        except mysql.connector.Error as err:
            print(f"Error, couldn't add animal to database: {err}")
            return None
            
    def deleteAnimal(self, animalID):
        try:
            instruction = f"DELETE FROM animal WHERE id = %s;"
            values = (animalID,)
            animal = self.findBy("id",animalID)
            if animal == None: return False
            self.connection.executeSQL(instruction, values)
            print("Animal deleted successfully!")
            return True
        except mysql.connector.Error as err:
            print(f"Error, couldn't delete animal from database: {err}")
            return None

    def findBy(self, category, value):
        try:
            instruction = f"SELECT * FROM animal WHERE {category} = %s;"
            values = (value,)
            rows = self.connection.executeSQL(instruction, values)
            results = []
            if rows == []: return None
            for row in rows:
                results.append(self.__convertToAnimal(row))
            return results
        except mysql.connector.Error as err:
            print(f"Error, couldn't find animal in database: {err}")
        return None
    
    def updateAnimal(self, animalID, updatedData, updatedValue):
        try:
            instruction = f"UPDATE animal SET "
            values = []

            if updatedData == "id": instruction += "id = %s"
            if updatedData == "name": instruction += "name = %s"
            if updatedData == "species": instruction += "species = %s"
            if updatedData == "diet": instruction += "diet = %s"
            if updatedData == "habitat": instruction += "habitat = %s"
            
            values.append(updatedValue)
            
            instruction += " WHERE id = %s;"
            values.append(animalID)

            self.connection.executeSQL(instruction, tuple(values))
            print("Animal updated successfully!")
            return True
        except mysql.connector.Error as err:
            print(f"Error, couldn't update animal in database: {err}")
            return None
            
    def findAll(self):
        try:
            instruction = f"SELECT * FROM animal;"
            rows=self.connection.executeSQL(instruction, None)
            return self.__convertToListOfAnimals(rows)
        except mysql.connector.Error as err:
            print(f"Error, couldn't retrieve animals from database: {err}")
            return None
        
    def __convertToAnimal(self, row):
            try:
                animal = Animal(row[0], row[1], row[2], row[3], row[4])
                return animal
            except Exception as e:
                print(f"Error converting row to Animal object: {e}")
                return None
            
    def __convertToListOfAnimals(self, rows):
        try:
            animals = []
            for row in rows:
                animal = self.__convertToAnimal(row)
                if animal:
                    animals.append(animal)
            return animals
        except Exception as e:
            print(f"Error converting list of rows to list of Animal objects: {e}")
            return []