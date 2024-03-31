from model.User import User
from model.repository.UserRepository import UserRepository


def testAddNewUser():
    userRepository = UserRepository("zootest")
    user = User(10,"Bety","i_love_food",1)
    
    status1 = userRepository.addUser(user)
    status2 = userRepository.addUser(user)
    
    assert status1 == True
    assert status2 == False
    
def testFindUser():
    userRepository = UserRepository("zootest")
    results1 = userRepository.findBy("id", 1)
    results2 = userRepository.findBy("id", 14)
    
    assert results1 != None
    assert len(results1) == 1
    assert results2 == None
    assert results1[0].username == "timo"
    
def testFindUsers():
    userRepository = UserRepository("zootest")
    results = userRepository.findBy("admin", 1)
    
    assert results != None
    assert len(results) == 2
    assert results[0].username == "timo"
    assert results[1].username == "josh"
    
def testFindAllusers():
    userRepository = UserRepository("zootest")
    results = userRepository.findAll()
    
    assert results != None
    assert len(results) == 4
    assert results[0].username == "timo"
    assert results[1].username == "floricel"
    assert results[2].username == "maria"
    assert results[3].username == "josh"
    
def testUpdateuser():
    userRepository = UserRepository("zootest")
    result = userRepository.updateUser(1,"username","timo_the_best")
    
    assert result == True
    
def testDeleteuser():
    userRepository = UserRepository("zootest")
    
    results1 = userRepository.deleteUser("2")
    results2 = userRepository.deleteUser("2")
    
    assert results1 == True
    assert results2 == False