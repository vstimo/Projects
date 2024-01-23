package mafia.Runnables.clientSideRunnables;

import mafia.InputThings.InputProducer;
/**
 * a runnable that constantly takes inputs from the client
 */
public class RunnableInputTaker implements Runnable{
    private InputProducer inputProducer;

    public RunnableInputTaker(InputProducer inputProducer){
        this.inputProducer = inputProducer;
    }
    @Override
    public void run() {
        inputProducer.startTakingInputs();
    }
}
