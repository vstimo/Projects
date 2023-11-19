package model;

/**
 * Clasa care descrie comanda facuta de un client care contine o cantitate specificata dintr-un SINGUR produs
 * field: id
 * field: idClient
 * field: clientName
 * field: idProduct
 * field: productName
 * field: quantity
 */
public class Order {
    private int id;
    private int idClient;
    private String clientName;
    private int idProduct;
    private String productName;
    private int quantity;

    public Order() {
    }

    public Order(int idClient, String clientName, int idProduct, String productName, int quantity) {
        this.idClient = idClient;
        this.clientName = clientName;
        this.idProduct = idProduct;
        this.productName = productName;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
