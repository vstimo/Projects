package mafia.roleThings.goodGuys;

import mafia.gui.ButtonClickListenerGame;
import mafia.gui.InGameGUI;
import mafia.InputThings.InputProducer;
import mafia.commandThings.Command;
import mafia.commandThings.CommandTypes;
import mafia.roleThings.Actionable;
import mafia.votingThings.VoteButtonsGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * class for the role , Mayor
 */
public class Mayor extends Actionable implements ButtonClickListenerGame {
    private InGameGUI gameGUI;
    public VoteButtonsGUI voteButtonsGUI;
    public String input;
    public Mayor(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream , String userName , InputProducer inputProducer, InGameGUI gameGUI) {
        super(objectInputStream, objectOutputStream , "mayor" , userName , inputProducer);
        this.gameGUI = gameGUI;
    }
    /**
     * the action of the player
     */
    @Override
    public void action(Command command) {
        startNow();
        boolean correctlyDone = false;

        ArrayList<String> options = new ArrayList<>();
        options.add("no");
        options.add("yes");

        voteButtonsGUI = new VoteButtonsGUI(options);
        voteButtonsGUI.addButtonClickListenerGame(this);

        input = "";
        Command actionCommand = null;
        while (!correctlyDone) {
            if (isTimeOver(getTimeLimit())) break;

            gameGUI.chatArea.append("SERVER: do you want to cancel the voting?\n");

            while (!isTimeOver(getTimeLimit())) {
                if (!input.equals("")) break;

                if (isTimeOver(getTimeLimit())) break;
            }

            if(input.equals("no")){
                gameGUI.chatArea.append("SERVER: ok, you didn't cancel the voting.\n");
                correctlyDone = true;
                actionCommand = new Command(CommandTypes.mayorSaysLynch , null);
                break;
            }
            else if(input.equals("yes")){
                gameGUI.chatArea.append("SERVER: ok, you canceled the voting!\n");
                correctlyDone = true;
                actionCommand = new Command(CommandTypes.mayorSaysDontLynch , null);
                break;
            }
        }
        if(!correctlyDone){
            gameGUI.chatArea.append("SERVER: time out\n");
            actionCommand = new Command(CommandTypes.mayorSaysLynch , null);
        }
        try {
            getObjectOutputStream().writeObject(actionCommand);
        } catch (IOException e) {
            gameGUI.setPopWindow("SERVER: you're disconnecting...", "connection lost", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        voteButtonsGUI.dispose();
        //----
        gameGUI.setEveryoneSleeps();
    }

    @Override
    public void buttonClickedByMayor() {
        input = voteButtonsGUI.pressedButton.getText();
    }

    @Override
    public void buttonClickedWhenReady() {}
    @Override
    public void buttonClickedWhenVoting() {}
    @Override
    public void buttonClickedByKiller() {}
    @Override
    public void buttonClickedByDetective() {}
    @Override
    public void buttonClickedByTherapist() {}
    @Override
    public void buttonClickedByDoctor() {}
}
