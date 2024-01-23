package mafia.Runnables.clientSideRunnables;

import mafia.gui.ButtonClickListenerGame;
import mafia.gui.InGameGUI;
import mafia.InputThings.InputProducer;
import mafia.gui.TextSenderListener;
import mafia.chatThings.Message;
import mafia.commandThings.Command;
import mafia.commandThings.CommandTypes;
import mafia.votingThings.VoteButtonsGUI;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * a class for the player to send his messages during the chat
 */
public class RunnableClientMessageSender implements Runnable, TextSenderListener, ButtonClickListenerGame {
    private String userName;
    private ObjectOutputStream objectOutputStream;

    private InputProducer inputProducer;
    private long startSecond;
    private long timeLimit;
    private InGameGUI gameGUI;
    public String input;
    public RunnableClientMessageSender(String userName, ObjectOutputStream objectOutputStream , InputProducer inputProducer, InGameGUI gameGUI) {
        this.userName = userName;
        this.objectOutputStream = objectOutputStream;
        this.inputProducer = inputProducer;
        this.timeLimit = 180;
        this.gameGUI = gameGUI;
    }

    @Override
    public void run() {
        startNow();
        input = "";
        boolean sentReady = false;
        gameGUI.addTextSenderListener(this);

        ArrayList<String> buttons = new ArrayList<>();
        buttons.add("skip discussion");

        VoteButtonsGUI voteButtonsGUI = new VoteButtonsGUI(buttons);
        voteButtonsGUI.addButtonClickListenerGame(this);

        do{
            if (isTimeOver(timeLimit)) break;

            if(input.equals("ready")) {
                try {
                    objectOutputStream.writeObject(new Command(CommandTypes.imReady , null));
                    sentReady = true;
                    input="";
                    break;
                } catch (IOException e) {
                    gameGUI.setPopWindow("SERVER: you're disconnecting...", "connection lost", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }
            else if (!input.equals("")){
                try {
                    objectOutputStream.writeObject(new Command(CommandTypes.messageToOthers , new Message(userName , input)));
                    input="";
                } catch (IOException e) {
                    gameGUI.setPopWindow("SERVER: you're disconnecting...", "connection lost", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }
        }while (!isTimeOver(timeLimit));

        if(!sentReady ){
            try {
                objectOutputStream.writeObject(new Command(CommandTypes.imReady , null));
            } catch (IOException e) {
                gameGUI.setPopWindow("SERVER: you're disconnecting...", "connection lost", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

        }
        SwingUtilities.invokeLater(() -> {
            voteButtonsGUI.dispose();
        });
    }

    public void startNow(){
        startSecond = java.time.Instant.now().getEpochSecond();
    }

    public boolean isTimeOver(long timeLimit){
        long nowSecond = java.time.Instant.now().getEpochSecond();
        if(nowSecond >= startSecond + timeLimit){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void sendText() {
        input = gameGUI.textField.getText();
    }

    @Override
    public void buttonClickedWhenReady() {
        input = "ready";
    }

    @Override
    public void buttonClickedByKiller() {}
    @Override
    public void buttonClickedByDetective() {}
    @Override
    public void buttonClickedByTherapist() {}
    @Override
    public void buttonClickedByDoctor() {}
    @Override
    public void buttonClickedByMayor() {}
    @Override
    public void buttonClickedWhenVoting() {}
}
