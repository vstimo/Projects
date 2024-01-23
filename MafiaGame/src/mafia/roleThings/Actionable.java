package mafia.roleThings;

import mafia.InputThings.InputProducer;
import mafia.commandThings.Command;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
/**
 * class for the role , Actionable
 */
public abstract class Actionable implements Role {
    private String userName;
    private String roleNameString;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private InputProducer inputProducer;
    private long startSecond;
    private long timeLimit = 180; //secunde = 3 minute

    public Actionable(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream ,
                      String roleNameString, String userName, InputProducer inputProducer){
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.roleNameString = roleNameString;
        this.userName = userName;
        this.inputProducer = inputProducer;
    }
    public abstract void action(Command command);

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String getRoleString() {
        return roleNameString;
    }

    /**
     * sets the start of action as seconds from 1970
     */
    public void startNow(){
        startSecond = java.time.Instant.now().getEpochSecond();
    }

    /**
     * checks if the time is over or not
     * @param timeLimit is the limit of doing the action
     * @return true if the time is over , else false
     */
    public boolean isTimeOver(long timeLimit){
        long nowSecond = java.time.Instant.now().getEpochSecond();
        if(nowSecond >= startSecond + timeLimit){
            return true;
        }
        else {
            return false;
        }
    }

    public long getTimeLimit() {
        return timeLimit;
    }
}