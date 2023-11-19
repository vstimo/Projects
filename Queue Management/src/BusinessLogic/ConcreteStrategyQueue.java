package BusinessLogic;
import Model.Queue;
import java.util.List;
public class ConcreteStrategyQueue implements Strategy {
    //Metoda pentru a adauga un client in coada in functie de coada cea mai scurta ca numar de clienti
    @Override
    public Queue addClient(List<Queue> queues) {
        Queue shortestQueue = null;
        int shortestQueueSize = Integer.MAX_VALUE;

        for (Queue queue : queues) {
            int queueSize = queue.getClients().length;
            if (queueSize < shortestQueueSize) {
                shortestQueueSize = queueSize;
                shortestQueue = queue;
            }
        }

        if (shortestQueue == null) {
            throw new IllegalMonitorStateException("Momentan nici o coada nu este disponibila!");
        }

        return shortestQueue;
    }
}
