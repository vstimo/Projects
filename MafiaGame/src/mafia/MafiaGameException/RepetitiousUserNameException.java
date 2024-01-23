package mafia.MafiaGameException;
/**
 * an exception for the situation , the username picked by the player , is repetitious
 */
public class RepetitiousUserNameException extends Exception{

    public RepetitiousUserNameException(String message){
        super(message);
    }

}
