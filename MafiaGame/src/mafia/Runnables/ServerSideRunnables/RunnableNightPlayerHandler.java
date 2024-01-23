package mafia.Runnables.ServerSideRunnables;

import mafia.commandThings.Command;
import mafia.commandThings.CommandTypes;
import mafia.serverThings.Server;
import mafia.serverThings.ServerSidePlayerDetails;

import java.io.IOException;

/**
 * runnable class for handling the night actions of the player
 * server side
 */
public class RunnableNightPlayerHandler implements Runnable{

    private ServerSidePlayerDetails player;

    /**
     * simple constructor
     * @param player is the player should do his action
     */
    public RunnableNightPlayerHandler(ServerSidePlayerDetails player){
        this.player = player;
    }

    /**
     * the run method for receiving the players action command
     */
    @Override
    public void run() {
        Command command = null;
        while (true){
            try {
                command = player.receivePlayerRespond();
                if(command == null){
                    break;
                }
                else if(command.getType() == CommandTypes.iDoMyAction){
                    Server.doTheCommand(command);
                    break;
                }
                else {
                    Server.doTheCommand(command);
                }
            } catch (IOException e) {
                Server.removeOfflinePlayerNotifyOthers(player);
                break;
            }
        }
    }
}
