package mafia.Runnables.clientSideRunnables;

import mafia.gui.ButtonClickListenerGame;
import mafia.InputThings.InputProducer;
import mafia.commandThings.Command;
import mafia.commandThings.CommandTypes;
import mafia.votingThings.Vote;
import mafia.votingThings.VoteButtonsGUI;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * runnable class for player to vote
 */
public class RunnableClientVote implements Runnable, ButtonClickListenerGame {
    private String voterName;
    private ObjectOutputStream objectOutputStream ;
    private ArrayList<String> otherPlayersNames;
    private InputProducer inputProducer;
    private long startSecond;
    private long timeLimit;
    public String input;
    public VoteButtonsGUI voteButtonsGUI;

    public RunnableClientVote(String voterName,ArrayList<String> otherPlayersNames,ObjectOutputStream objectOutputStream,InputProducer inputProducer){
        this.voterName = voterName;
        this.otherPlayersNames = otherPlayersNames;
        this.objectOutputStream = objectOutputStream;
        this.inputProducer = inputProducer;
        this.timeLimit = 60;
    }

    /**
     * the run method for voting of the player
     */
    @Override
    public void run() {
        otherPlayersNames.remove(voterName);
        startNow();
        boolean correctlyDone = false;
        input = "";
        Command voteCommand = null;

        ArrayList<String> buttons = new ArrayList<>();
        buttons.add("nobody");
        for(int i = 1; i <= otherPlayersNames.size(); i++)
            buttons.add(otherPlayersNames.get(i-1));

        voteButtonsGUI = new VoteButtonsGUI(buttons);
        voteButtonsGUI.addButtonClickListenerGame(this);

        do{
            if (isTimeOver(timeLimit)) break;

            if(input.equals("nobody")){
                voteCommand = new Command(CommandTypes.iVote , new Vote(voterName , null));
                correctlyDone = true;
                break;
            }
            else if(!input.equals("")) {
                {
                    voteCommand = new Command(CommandTypes.iVote, new Vote(voterName, input));
                    correctlyDone = true;
                    break;
                }
            }

        }while (!correctlyDone && !isTimeOver(timeLimit));

        if(!correctlyDone)
            voteCommand = new Command(CommandTypes.iVote , new Vote(voterName , null));

        try {
            objectOutputStream.writeObject(voteCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
        voteButtonsGUI.dispose();
    }

    /**
     * saves the start moment of the game
     */
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
    public void buttonClickedWhenVoting() {
        input = voteButtonsGUI.pressedButton.getText();
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
    public void buttonClickedWhenReady() {}
}
