package mafia.Runnables.clientSideRunnables;

import mafia.commandThings.Command;
import mafia.roleThings.Actionable;

/**
 * runnable for player to do the action of his role
 */
public class RunnableActionDoer implements Runnable{
    private Actionable actionable;
    private Command command;

    public RunnableActionDoer(Actionable actionable , Command command){
        this.actionable = actionable;
        this.command = command;
    }

    @Override
    public void run() {
        actionable.action(command);
    }
}
