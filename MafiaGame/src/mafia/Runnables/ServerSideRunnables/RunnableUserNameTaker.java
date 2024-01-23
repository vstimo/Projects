package mafia.Runnables.ServerSideRunnables;


import mafia.MafiaGameException.RepetitiousUserNameException;
import mafia.commandThings.Command;
import mafia.commandThings.CommandTypes;
import mafia.serverThings.Server;
import mafia.serverThings.ServerMafiaGameLogic;
import mafia.serverThings.ServerSidePlayerDetails;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

/**
 * runnable class for setting the userName of the player
 * server side
 */
public class RunnableUserNameTaker implements Runnable{
    private ServerSidePlayerDetails thisPlayerDetails;
    private ServerMafiaGameLogic serverMafiaGameLogic;

    public RunnableUserNameTaker(ServerMafiaGameLogic serverMafiaGameLogic, ServerSidePlayerDetails thisPlayerDetails){
        this.serverMafiaGameLogic = serverMafiaGameLogic;
        this.thisPlayerDetails = thisPlayerDetails;
    }

    /**
     * run method for taking username from the player and setting it
     */
    @Override
    public void run() {

        String inputUserName = null;
        Command clientRespond = null;
        ObjectOutputStream objectOutputStream = thisPlayerDetails.getObjectOutputStream();
        ObjectInputStream objectInputStream = thisPlayerDetails.getObjectInputStream();
        boolean userNameIsSet = false;

        while (!userNameIsSet){

            try {
                clientRespond = (Command) objectInputStream.readObject();

                if(clientRespond.getType() == CommandTypes.setMyUserName){
                     inputUserName = (String) clientRespond.getCommandNeededThings();
                     serverMafiaGameLogic.setUserName(thisPlayerDetails , inputUserName);
                     objectOutputStream.writeObject(new Command(CommandTypes.yourUserNameIsSet ,inputUserName));

                     userNameIsSet = true;
                     break;
                }
                else{
                   serverMafiaGameLogic.doTheCommand(new Command(CommandTypes.iExitTheGame , thisPlayerDetails));
                    break;
                }

            }catch (SocketException e){
                Server.removeOfflinePlayerNotifyOthers(thisPlayerDetails);
                break;
            }catch (IOException e) {
                Server.removeOfflinePlayerNotifyOthers(thisPlayerDetails);
                break;
            } catch (ClassNotFoundException e) {
                System.out.println("wrong sent from player : " + thisPlayerDetails.getUserName());
                Server.removeOfflinePlayerNotifyOthers(thisPlayerDetails);
                break;
            } catch (RepetitiousUserNameException e) {

                try {
                    objectOutputStream.writeObject(new Command(CommandTypes.repetitiousUserName ,
                            "this username is already taken"));

                } catch (IOException ioException) {
                    Server.removeOfflinePlayerNotifyOthers(thisPlayerDetails);
                    break;
                }
            }
        }
    }
}
