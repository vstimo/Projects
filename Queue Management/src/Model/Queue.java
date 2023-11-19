package Model;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
public class Queue implements Runnable {
    private final BlockingQueue<Client> clients;
    private final AtomicInteger waitingPeriod; //utilizam AtomicInteger pentru a evita interferenta thread-urilor
    private boolean isRunning; //variabila care controleaza oprirea fortata a tuturor coziilor
    public Queue() {
        clients = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger();
        isRunning = true;
    }
    public void addClient(Client client) { //adaugam clientul si actualizam timpul total de asteptare la coada
        clients.add(client);
        waitingPeriod.addAndGet(client.getServiceTime());
    }
    public void run() {
        while (isRunning) {
            try {
                Client client = clients.peek();
                if (client != null) {
                    Thread.sleep(client.getServiceTime() * 1000L); //oprim temporar threadul pentru a servi clientul curent
                    clients.take();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    public Client[] getClients() {
        return clients.toArray(new Client[0]);
    }
    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }
    public void decrementWaitingPeriod() {
        waitingPeriod.addAndGet(-1); //in fiecare secunda scadem cu o unitate din timpul total de asteptare al cozii
    }
    public void stopQueue() {
        isRunning = false;
    }
}