package BusinessLogic;
import Model.Queue;
import java.util.List;
public class ConcreteStrategyTime implements Strategy {
    //Metoda pentru a adauga un client in coada in functie de coada cea mai scurta ca timp
    @Override
    public Queue addClient(List<Queue> queues) {
        Queue selectedQueue = null;
        int minWaitingPeriod = Integer.MAX_VALUE;

        for (Queue queue : queues) {
            int queueWaitingPeriod = queue.getWaitingPeriod();
            if (queueWaitingPeriod < minWaitingPeriod) {
                selectedQueue = queue;
                minWaitingPeriod = queueWaitingPeriod;
            }
        }

        if (selectedQueue == null) {
            throw new IllegalStateException("Momentan nici o coada nu este disponibila!");
        }

        return selectedQueue;
    }
}
