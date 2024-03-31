import mysql.connector

class Connection():
    def __init__(self, *args):
        self.host = "localhost"
        self.username = "root"
        self.password = "7E49f2bdabc!"
        self.connection = None
        self.cursor = None
        if len(args) == 1: self.database=args[0]
        else: self.database="zoodb"
        
    def connectToDatabase(self):
        try:
            self.connection = mysql.connector.connect(
                host=self.host,
                user=self.username,
                password=self.password,
                database=self.database
            )
            self.cursor=self.connection.cursor()
            print("Connected to database successfully!")
        except mysql.connector.Error as err:
            print(f"Error, couldn't connect to database: {err}")
            
    def createTable(self, tableName):
        try:
            cursor = self.connection.cursor()
            
            if tableName == "animal":
                cursor.execute(f"DROP TABLE IF EXISTS {self.database}.{tableName}")
                print(f"{tableName} table deleted successfully")
                
                cursor.execute(f"""
                    CREATE TABLE IF NOT EXISTS {self.database}.{tableName} (
                        id INT PRIMARY KEY NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        species VARCHAR(255) NOT NULL,
                        diet VARCHAR(255) NOT NULL,
                        habitat VARCHAR(255) NOT NULL
                    )""")
                print(f"{tableName} table created successfully")
           
                cursor.execute("""
                    INSERT INTO animal (id, name, species, diet, habitat) VALUES
                    (1, 'Simba', 'Lion', 'Carnivore', 'Savana'),
                    (2, 'Dumbo', 'Elephant', 'Herbivore', 'Jungle'),
                    (3, 'Longneck', 'Giraffe', 'Herbivore', 'Savana'),
                    (4, 'Stripes', 'Tiger', 'Carnivore', 'Jungle')
                """)
                self.connection.commit()
                print("Animals inserted successfully")
                
            elif tableName == "workers":
                cursor.execute(f"DROP TABLE IF EXISTS {self.database}.{tableName}")
                print(f"{tableName} table deleted successfully")
                
                cursor.execute(f"""
                    CREATE TABLE IF NOT EXISTS {self.database}.{tableName} (
                        id INT PRIMARY KEY NOT NULL,
                        username VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        admin TINYINT(1) NOT NULL
                    )""")
                print(f"{tableName} table created successfully")
                
                cursor.execute("""
                    INSERT INTO workers (id, username, password, admin) VALUES
                    (1, 'timo', 'parola_mia', 1),
                    (2, 'floricel', 'privesc_ploaia', 0),
                    (3, 'maria', 'curcubeu', 0),
                    (4, 'josh', 'life', 1);
                """)
                self.connection.commit()
                print("Workers inserted successfully")
                
        except mysql.connector.Error as err:
            print(f"Error: {err}")
            return False
        finally:
            cursor.close()
        return True

    
    def executeSQL(self, sql, values):
        try:
            self.cursor.execute(sql, values)
            rows = self.cursor.fetchall()
            print("SQL command executed successfully!")
            self.connection.commit()
            if rows!=None: return rows
            else: return None
        except mysql.connector.Error as err:
            print(f"Error, couldn't execute the SQL command: {err}")
            return None

    def closeConnection(self):
        try:
            self.connection.close()
            print("Connection closed successfully!")
        except mysql.connector.Error as err:
            print(f"Error, couldn't disconnect from database: {err}")
            
    def getConnection(self):
        return self.connection