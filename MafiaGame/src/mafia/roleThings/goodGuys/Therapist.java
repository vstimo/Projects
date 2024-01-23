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
 * class for the role, Therapist
 */
public class Therapist extends Actionable implements ButtonClickListenerGame {
    private InGameGUI gameGUI;
    private VoteButtonsGUI voteButtonsGUI;
    public String input;
    public Therapist(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream , String userName , InputProducer inputProducer, InGameGUI gameGUI) {
        super(objectInputStream, objectOutputStream , "therapist", userName , inputProducer);
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

        ArrayList<String> othersNames = (ArrayList<String>) command.getCommandNeededThings();
        gameGUI.setPlayersPosition(othersNames);
        gameGUI.setNight();
        othersNames.remove(getUserName());

        for (String nume : othersNames)
            auxiliar.add(nume);
        voteButtonsGUI = new VoteButtonsGUI(auxiliar);
        voteButtonsGUI.addButtonClickListenerGame(this);

        input = "";
        Command actionCommand = null;
        while (! correctlyDone){
            if (isTimeOver(getTimeLimit())) break;

            gameGUI.chatArea.append("SERVER: choose someone to mute next day!\n");

            while (!isTimeOver(getTimeLimit())) {
                if (!input.equals("")) break;

                if (isTimeOver(getTimeLimit())) break;
            }

            if (input.equals("skip")) {
                gameGUI.chatArea.append("SERVER: you mute nobody tonight.\n");
                correctlyDone = true;
                actionCommand = new Command(CommandTypes.iDoMyAction, new PlayerAction(PlayersActionTypes.mute, null));
                break;
            }
            else if (othersNames.contains(input)){
                gameGUI.chatArea.append("SERVER: you muted player: " + input + " tonight.\n");
                correctlyDone = true;
                actionCommand = new Command(CommandTypes.iDoMyAction, new PlayerAction(PlayersActionTypes.mute , input));
            }
        }
        if(! correctlyDone){
            gameGUI.chatArea.append("SERVER: time out\n");
            actionCommand = new Command(CommandTypes.iDoMyAction, new PlayerAction(PlayersActionTypes.mute, null));
        }

        try {
            getObjectOutputStream().writeObject(actionCommand);
        } catch (IOException e) {
            gameGUI.setPopWindow("SERVER: you're disconnecting...", "connection lost", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        voteButtonsGUI.dispose();
    }

    @Override
    public void buttonClickedByTherapist() {
        input = voteButtonsGUI.pressedButton.getText();
    }

    @Override
    public void buttonClickedByKiller() {}
    @Override
    public void buttonClickedByDetective() {}
    @Override
    public void buttonClickedByDoctor() {}
    @Override
    public void buttonClickedByMayor() {}
    @Override
    public void buttonClickedWhenReady() {}
    @Override
    public void buttonClickedWhenVoting() {}
}
