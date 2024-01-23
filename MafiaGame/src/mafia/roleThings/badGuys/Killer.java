package mafia.roleThings.badGuys;

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
 * a class for killer role
 */
public class Killer extends Actionable implements ButtonClickListenerGame{
    private InGameGUI gameGUI;
    private VoteButtonsGUI voteButtonsGUI;
    public String input;
    public Killer(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, String userName, InputProducer inputProducer, InGameGUI gameGUI) {
        super(objectInputStream, objectOutputStream, "killer", userName, inputProducer);
        this.gameGUI = gameGUI;
    }

    /**
     * the action of the role
     */
    @Override
    public void action(Command command) {
        startNow();
        boolean correctlyDone = false;

        ArrayList<String> auxiliar = new ArrayList<>();
        auxiliar.add("skip");

        ArrayList<String> othersNames = (ArrayList<String>) command.getCommandNeededThings();
        othersNames.add(getUserName());
        gameGUI.setPlayersPosition(othersNames);
        gameGUI.setNight();
        othersNames.remove(getUserName());

        for (String nume : othersNames)
            auxiliar.add(nume);
        voteButtonsGUI = new VoteButtonsGUI(auxiliar);
        voteButtonsGUI.addButtonClickListenerGame(this);

        input = "";
        Command actionCommand = null;
        while (!correctlyDone) {
            if (isTimeOver(getTimeLimit())) break;

            gameGUI.chatArea.append("SERVER: choose someone to kill tonight!\n");

            while (!isTimeOver(getTimeLimit())) {
                if (!input.equals("")) break;

                if (isTimeOver(getTimeLimit())) break;
            }

            if (input.equals("skip")) {
                gameGUI.chatArea.append("SERVER: you kill nobody tonight.\n");
                correctlyDone = true;
                actionCommand = new Command(CommandTypes.iDoMyAction, new PlayerAction(PlayersActionTypes.killerVictim, null));
                break;
            } else if (othersNames.contains(input)) {
                gameGUI.chatArea.append("SERVER: mafia kills player: " + input + " tonight.\n");
                correctlyDone = true;
                actionCommand = new Command(CommandTypes.iDoMyAction, new PlayerAction(PlayersActionTypes.killerVictim, input));
            }
        }
        if (!correctlyDone) {
            gameGUI.chatArea.append("SERVER: time out\n");
            actionCommand = new Command(CommandTypes.iDoMyAction, new PlayerAction(PlayersActionTypes.killerVictim,  null));
        }

        try {
            getObjectOutputStream().writeObject(actionCommand);
        } catch (
                IOException e) {
            gameGUI.setPopWindow("SERVER: you're disconnecting...", "connection lost", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        voteButtonsGUI.dispose();
    }
    @Override
    public void buttonClickedByKiller() {
        input = voteButtonsGUI.pressedButton.getText();
    }

    @Override
    public void buttonClickedByDetective() {}
    @Override
    public void buttonClickedByTherapist() {}
    @Override
    public void buttonClickedByDoctor() {}
    @Override
    public void buttonClickedByMayor() {}
    @Override
    public void buttonClickedWhenReady() {}
    @Override
    public void buttonClickedWhenVoting() {}
}
