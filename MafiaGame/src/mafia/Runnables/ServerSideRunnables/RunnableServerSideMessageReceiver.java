package mafia.Runnables.ServerSideRunnables;

import mafia.chatThings.Message;
import mafia.commandThings.Command;
import mafia.commandThings.CommandTypes;
import mafia.serverThings.Server;
import mafia.serverThings.ServerSidePlayerDetails;

import java.io.IOException;
/**
 * runnable class for receiving messages from player
 * server side
 */
public class RunnableServerSideMessageReceiver implements Runnable{
    private ServerSidePlayerDetails player;
    public RunnableServerSideMessageReceiver(ServerSidePlayerDetails player){
        this.player = player;
    }

    /**
     * the run method for receiving messages from the client
     */
    @Override
    public void run() {

        boolean isReady = false;

        Command command = null;
        while (! isReady){

            try {
                command = player.receivePlayerRespond();
                if(command.getType() == CommandTypes.imReady){
                    Server.doTheCommand(new Command(CommandTypes.messageToOthers , new Message(player.getUserName() , "I am ready to vote!")));
                    isReady = true;
                }

                else if(command.getType() == CommandTypes.messageToOthers){
                    Server.doTheCommand(command);
                }
            } catch (IOException e) {
                Server.removeOfflinePlayerNotifyOthers(player);
                break;
            }
        }
    }
}
