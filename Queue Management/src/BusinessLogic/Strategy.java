package BusinessLogic;

import Model.Queue;
import java.util.List;

public interface Strategy {
    //Interfata care in functie de strategia aleasa insereaza un client intr-o coada
    public Queue addClient(List<Queue> queues);
}
