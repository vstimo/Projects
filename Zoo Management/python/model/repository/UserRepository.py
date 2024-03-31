import mysql.connector

from model.User import User
from model.repository.Connection import Connection

class UserRepository:
    def __init__(self, *args):
        if len(args)==1:    
            self.connection = Connection(args[0])
            self.connection.connectToDatabase()
            self.connection.createTable("workers")
        else: 
            self.connection = Connection()
            self.connection.connectToDatabase()
        
    def addUser(self, user):
        try:
            instruction = f"INSERT INTO workers (id, username, password, admin) VALUES (%s, %s, %s, %s);"
            values = (user.getID(), user.getUsername(), user.getPassword(), user.getAdmin())
            status=self.connection.executeSQL(instruction, values)
            if status==None: return False
            print("User added successfully!")
            return True
        except mysql.connector.Error as err:
            print(f"Error, couldn't add user to database: {err}")
            return False
            
    def deleteUser(self, user_id):
        try:
            instruction = f"DELETE FROM workers WHERE id = %s;"
            values = (user_id,)
            user = self.findBy("id",user_id)
            if user == None: return False
            self.connection.executeSQL(instruction, values)
            print("User deleted successfully!")
            return True
        except mysql.connector.Error as err:
            print(f"Error, couldn't delete user from database: {err}")
            return False

    def findBy(self, category, value):
        try:
            instruction = f"SELECT * FROM workers WHERE {category} = %s;"
            values = (value,)
            rows = self.connection.executeSQL(instruction, values)
            results = []
            if rows == []: return None
            if rows != None:
                for row in rows:
                    results.append(self.__convertToUser(row))
                return results
        except mysql.connector.Error as err:
            print(f"Error, couldn't find user in database: {err}")
            return None
    
    def updateUser(self, userID, updatedData, updatedValue):
        try:
            instruction = f"UPDATE workers SET "
            values = []

            if updatedData == "id": instruction += "id = %s"
            if updatedData == "username": instruction += "username = %s"
            if updatedData == "password": instruction += "password = %s"
            if updatedData == "admin": 
                if updatedValue == True: updatedValue=1
                elif updatedValue==False: updatedValue=0
                instruction += "admin = %s"
                
            values.append(updatedValue)
            instruction += " WHERE id = %s;"
            values.append(userID)

            self.connection.executeSQL(instruction, tuple(values))
            print("User updated successfully!")
            return True
        except mysql.connector.Error as err:
            print(f"Error, couldn't update user in database: {err}")
            return False
            
    def findAll(self):
        try:
            instruction = f"SELECT * FROM workers;"
            rows = self.connection.executeSQL(instruction, None)
            return self.__convertToListOfUsers(rows)
        except mysql.connector.Error as err:
            print(f"Error, couldn't retrieve users from database: {err}")
            return None
        
    def __convertToUser(self, row):
        try:
            if row:
                user = User(row[0], row[1], row[2], row[3])
                return user
            return None
        except Exception as e:
            print(f"Error converting row to User object: {e}")
            return None
            
    def __convertToListOfUsers(self, rows):
        try:
            users = []
            for row in rows:
                user = self.__convertToUser(row)
                if user:
                    users.append(user)
            return users
        except Exception as e:
            print(f"Error converting list of rows to list of User objects: {e}")
            return []