package model;

/**
 * Clasa care descrie clientul
 * field: id
 * field: name
 * field: adress
 * field: email
 * field: age
 */
public class Client {
    private int id;
    private String name;
    private String adress;
    private String email;
    private int age;

    public Client() {
    }

    public Client(String name, String adress, String email, int age) {
        this.name = name;
        this.adress = adress;
        this.email = email;
        this.age = age;
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
