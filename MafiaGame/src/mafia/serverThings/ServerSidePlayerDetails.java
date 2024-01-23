package mafia.serverThings;


import mafia.commandThings.Command;
import mafia.commandThings.CommandTypes;
import mafia.roleThings.RoleNames;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
/**
 * class for ServerSidePlayerDetails
 */
public class ServerSidePlayerDetails implements Serializable {
    private Socket socket;
    private String userName;
    private RoleNames roleName;
    private boolean isMuted;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public ServerSidePlayerDetails(Socket socket, ObjectOutputStream objectOutputStream , ObjectInputStream objectInputStream){
        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
    }

    public String getUserName() {
        return userName;
    }

    public RoleNames getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleNames roleName) {
        this.roleName = roleName;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void sendCommandToPlayer(Command command) throws IOException {
            objectOutputStream.writeObject(command);
    }

    /**
     * waits for the respond of the player
     * @return the command from the player as respond
     * @throws IOException if there is a problem with connection
     */
    public Command receivePlayerRespond() throws IOException {
        Command respond = null;
        try {
            respond = (Command) objectInputStream.readObject();
        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return respond;
    }

    public void sendTheRoleToThePlayer(RoleNames roleName) throws IOException {
        objectOutputStream.writeObject(new Command(CommandTypes.takeYourRole , roleName));
    }
}
