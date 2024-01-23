package mafia.roleThings.goodGuys;

import mafia.gui.ButtonClickListenerGame;
import mafia.gui.InGameGUI;
import mafia.InputThings.InputProducer;
import mafia.commandThings.Command;
import mafia.commandThings.CommandTypes;
import mafia.night.PlayerAction;
import mafia.night.PlayersActionTypes;
import mafia.roleThings.Actionable;
import mafia.votingThings.VoteButtonsGUI;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
/**
 * class for the role , TownDoctor
 */
public class TownDoctor extends Actionable implements ButtonClickListenerGame {
    private InGameGUI gameGUI;
    private VoteButtonsGUI voteButtonsGUI;
    public String input;
    public TownDoctor(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream , String userName , InputProducer inputProducer, InGameGUI gameGUI) {
        super(objectInputStream, objectOutputStream , "town doctor" , userName , inputProducer);
        this.gameGUI = gameGUI;

    }
    /**
     * the action of the player
     */
    @Override
    public void action(Command command) {
        startNow();
        boolean correctlyDone = false;

        ArrayList<String> auxiliar = new ArrayList<>();
        auxiliar.add("skip");

        ArrayList<String> savablePlayersNames = (ArrayList<String>) command.getCommandNeededThings();
        gameGUI.setPlayersPosition(savablePlayersNames);
        gameGUI.setNight();

        for (String nume : savablePlayersNames)
            auxiliar.add(nume);
        voteButtonsGUI = new VoteButtonsGUI(auxiliar);
        voteButtonsGUI.addButtonClickListenerGame(this);

        input = "";
        Command saveCommand = null;
        while (!correctlyDone) {
            if (isTimeOver(getTimeLimit())) break;

            gameGUI.chatArea.append("SERVER: choose someone to save tonight!\n");

            while (!isTimeOver(getTimeLimit())) {
                if (!input.equals("")) break;

                if (isTimeOver(getTimeLimit())) break;
            }

            if (input.equals("skip")) {
                gameGUI.chatArea.append("SERVER: you save nobody tonight.\n");
                correctlyDone = true;
                saveCommand = new Command(CommandTypes.iDoMyAction,new PlayerAction(PlayersActionTypes.townDoctorSave, null));
                break;
            } else if (savablePlayersNames.contains(input)) {
                if(input.equals(getUserName())){
                    gameGUI.chatArea.append("SERVER: you save yourself tonight\n");
                    correctlyDone = true;
                    saveCommand = new Command(CommandTypes.iDoMyAction, new PlayerAction(PlayersActionTypes.townDoctorSave, input));
                }
                else{
                    gameGUI.chatArea.append("SERVER: you save: " + input + " tonight.\n");
                    correctlyDone = true;
                    saveCommand = new Command(CommandTypes.iDoMyAction, new PlayerAction(PlayersActionTypes.townDoctorSave, input));
                }
            }
        }

        if (! correctlyDone){
            gameGUI.chatArea.append("SERVER: time out\n");
            saveCommand = new Command(CommandTypes.iDoMyAction, new PlayerAction(PlayersActionTypes.townDoctorSave, null));
        }

        try {
            getObjectOutputStream().writeObject(saveCommand);
        } catch (IOException e) {
            gameGUI.setPopWindow("SERVER: you're disconnecting...", "connection lost", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        voteButtonsGUI.dispose();
    }

    @Override
    public void buttonClickedByDoctor() {
        input = voteButtonsGUI.pressedButton.getText();
    }

    @Override
    public void buttonClickedByMayor() {}
    @Override
    public void buttonClickedWhenReady() {}
    @Override
    public void buttonClickedWhenVoting() {}
    @Override
    public void buttonClickedByKiller() {

    }
    @Override
    public void buttonClickedByDetective() {

    }
    @Override
    public void buttonClickedByTherapist() {

    }
}
