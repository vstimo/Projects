package mafia.InputThings;

import mafia.gui.InGameGUI;
import mafia.gui.Lobby;

import java.util.Scanner;

/**
 * makes the client to enter the valid input, if enters something else, repeats
 */
public class LoopedTillRightInput {
    private Scanner scanner;
    public Lobby lobby;
    public InGameGUI gameGUI;

    public LoopedTillRightInput(){
        scanner = new Scanner(System.in);
    } //server

    public LoopedTillRightInput(Lobby lobby, InGameGUI gameGUI){
        this.lobby = lobby;
        this.gameGUI = gameGUI;
    } //client

    /**
     * scans a string from system input
     * @return the input string , but if it is "exit" closes the game
     */
    public String stringInput(){
        String input = scanner.nextLine();
        return input;
    }

    public String stringInputL(){
        String input = lobby.getPlayerName();
        return input;
    }

    public int intInput(){
        boolean doneCorrectly = false;
        int input = 0;
        while (! doneCorrectly){
            try{
                input = Integer.parseInt(stringInput());
                doneCorrectly = true;
            }catch (NumberFormatException e){
                System.err.println("the input is not a number , please try again : ");
                doneCorrectly = false;
            }
        }
        return input;
    }

    public int rangedIntInput(int minimum , int maximum){
        boolean doneCorrectly = false;
        int input = 0;
        while (! doneCorrectly){
            input = intInput();
            if(input > maximum || input < minimum){
                System.out.println("the input is not in range (minimum : " + minimum + " & maximum : " +maximum + " ) " );
            }
            else{
                doneCorrectly = true;
            }
        }
        return input;
    }
}
