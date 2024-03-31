from model.repository.Connection import Connection

def testConnection():  
    connection = Connection("zootest")
    connection.connectToDatabase()
    assert connection.getConnection().is_connected() == True
    
def testCreateTable():
    connection = Connection("zootest")
    connection.connectToDatabase()
    status=connection.createTable("animal")
    assert status == True

def testExecuteSQL():
    connection = Connection("zootest")
    connection.connectToDatabase()
    sql = "SELECT * FROM animal"
    values = ()
    result = connection.executeSQL(sql, values)
    assert result is not None

def testCloseConnection():
    connection = Connection("zootest")
    connection.connectToDatabase()
    connection.closeConnection()
    assert connection.getConnection().is_connected() == False