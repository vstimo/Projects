package mafia.commandThings;

import java.io.Serializable;
/** is a class for communication between server and client
 */
public class Command implements Serializable {
    private CommandTypes title;
    private Serializable CommandNeededThings;

    public Command(CommandTypes title, Serializable commandNeededThings) {
        this.title = title;
        CommandNeededThings = commandNeededThings;
    }

    public CommandTypes getType() {
        return title;
    }

    public Serializable getCommandNeededThings() {
        return CommandNeededThings;
    }

}
