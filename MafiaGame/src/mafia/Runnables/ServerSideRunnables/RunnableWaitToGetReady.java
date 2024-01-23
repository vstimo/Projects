package mafia.Runnables.ServerSideRunnables;

import mafia.commandThings.Command;
import mafia.commandThings.CommandTypes;
import mafia.serverThings.Server;
import mafia.serverThings.ServerSidePlayerDetails;

import java.io.IOException;

/**
 * runnable class to wait for the player to get ready to play
 */
public class RunnableWaitToGetReady implements Runnable{
    private ServerSidePlayerDetails player;

    public RunnableWaitToGetReady(ServerSidePlayerDetails player){
        this.player = player;
    }

    /**
     * run method for waiting for player to get ready
     */
    @Override
    public void run(){

        boolean gotReady = false;

        try {
            player.sendCommandToPlayer(new Command(CommandTypes.waitingForClientToGetReady , null));
        } catch (IOException e) {
            Server.removeOfflinePlayerNotifyOthers(player);
        }

        while (!gotReady){
            try {
                Command command = player.receivePlayerRespond();

                if(command.getType() == CommandTypes.imReady){
                    gotReady = true;
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
