package mafia.Runnables.ServerSideRunnables;

import mafia.commandThings.Command;
import mafia.commandThings.CommandTypes;
import mafia.serverThings.Server;
import mafia.serverThings.ServerSidePlayerDetails;

import java.awt.*;
import java.io.IOException;
/**
 * runnable class for handling the vote from the player
 * server side
 */
public class RunnableVoteHandler implements Runnable{

    private ServerSidePlayerDetails voter;

    public RunnableVoteHandler(ServerSidePlayerDetails voter){
        this.voter = voter;
    }

    @Override
    public void run() {
        boolean hasVoted = false;

        while (!hasVoted){
            Command command = null;
            try {
                command = voter.receivePlayerRespond();
                if(command.getType() == CommandTypes.iVote){
                    hasVoted = true;
                    Server.doTheCommand(command);
                    break;
                }

                else{
                    Server.doTheCommand(command);
                }
            } catch (IOException e) {
                Server.removeOfflinePlayerNotifyOthers(voter);
                break;
            }
        }
    }
}
