package Model;
public class Client {
    private final int idClient, arrivalTime;
    private int serviceTime;
    public Client(int idClient, int arrivalTime, int serviceTime) {
        this.idClient = idClient;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }
    public int getId() {
        return idClient;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getServiceTime() {
        return serviceTime;
    }
    public void setServiceTime() { //metoda pentru a actualiza cat mai dureaza servirea clientului curent (adica din fruntea cozii)
        if (serviceTime >= 1) serviceTime--;
    }
    @Override
    public String toString() {
        return "(id: " + idClient + ", arrivalTime: " + arrivalTime + ", serviceTime: " + serviceTime + ")";
    }
}