package mafia.InputThings;

import mafia.gui.ButtonClickListener;

import java.util.ArrayList;

/**
 * the class for store , consume the inputs of the client ( technically a buffer )
 */
public class InputProducer implements ButtonClickListener {
    private ArrayList<String> inputs = null;
    private LoopedTillRightInput loopedTillRightInput;

    public InputProducer(LoopedTillRightInput loopedTillRightInput){
        inputs = new ArrayList<>();
        this.loopedTillRightInput = loopedTillRightInput;
    }

    public void startTakingInputs(){
        //
    }

    /**
     * adds the input string in the inputs arrayList
     * @param input is the input string
     */
    private void storeInput(String input){
        synchronized (input){
            if (!input.equals("")) inputs.add(input);
        }
    }

    /**
     * @return true if there is at least one input , else false
     */
    public boolean hasNext(){
        synchronized (inputs){
            if(inputs.size() > 0){
                return true;
            }
            return false;
        }
    }

    /**
     * @return the first existing input from the arrayList and removes it from the input arrayList
     */
    public String consumeInput(){
        String consumingString = null;
        if(hasNext()){
            synchronized (inputs){
                consumingString = inputs.get(0);
                inputs.remove(0);
            }
        }
        return consumingString;
    }

    public void clearInputs(){
        synchronized (inputs){
            inputs = new ArrayList<>();
        }
    }
    @Override
    public void submitButtonClicked() {
        String input = loopedTillRightInput.stringInputL();
        storeInput(input);
    }
    @Override
    public void readyButtonClicked() {
        storeInput("ready");
    }
}