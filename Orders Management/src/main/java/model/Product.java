package model;

/**
 * Clasa care descrie produsul
 * field: id
 * field: name
 * field: stock
 * field: price
 */
public class Product {
    private int id;
    private String name;
    private int stock;
    private int price;

    public Product() {
    }

    public Product(String name, int stock, int price) {
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void updateStock(int consumed) { //dupa ce a fost plasata comanda, stock-ul din produsul comandat se actualizeaza
        if (stock >= consumed) this.stock = this.stock - consumed;
    }
}
