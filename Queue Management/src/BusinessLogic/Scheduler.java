package BusinessLogic;
import Model.Queue;
import Model.Client;
import java.util.ArrayList;
import java.util.List;
public class Scheduler {
    private List<Queue> queues;
    private Strategy strategy;
    public Scheduler(int nrQueues) {
        queues = new ArrayList<>();
        //Pentru fiecare coada ii cream si propriul thread
        for (int i = 0; i < nrQueues; i++) {
            Queue queue = new Queue();
            Thread thread = new Thread(queue);
            thread.start();
            queues.add(queue);
        }
    }
    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE)
            strategy = new ConcreteStrategyQueue();
        if (policy == SelectionPolicy.SHORTEST_TIME)
            strategy = new ConcreteStrategyTime();
    }
    public void dispatchClient(Client client) {
        //Din strategy return coada selectata ca find optima, iar mai apoi adaugam clientul in aceasta
        Queue selectedQueue = strategy.addClient(queues);
        selectedQueue.addClient(client);
    }
    public List<Queue> getQueues() {
        return queues;
    }
    //Metoda prin care oprim toate coziile din functionare
    public void stopAll() {
        for (Queue queue : queues)
            queue.stopQueue();
    }
}