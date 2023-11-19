package businessLogic;
import businessLogic.validators.QuantityValidator;
import businessLogic.validators.Validator;
import dao.OrderDAO;
import model.Client;
import model.Order;
import model.Product;
import presentation.OrderGUI;
import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Aceasta clasa contine logica din spate. Folosind metodele definite in OrderDAO se creaza noi metode cu functionalitati similare conceptului
 * de OOP dar care au la baza teorie de MySQL si care sunt mai departe utilizate in clasele pentru GUI
 */
public class OrderBLL {
    private List<Validator<Order>> validators;
    private OrderDAO orderDAO;

    public OrderBLL() {
        validators = new ArrayList<Validator<Order>>(); //nu avem validator pentru ID, idClient, idProduct si totalPrice deoarece am considerat ca nu este necesar
        validators.add(new QuantityValidator());
        orderDAO = new OrderDAO();
    }

    public List<Order> findAllOrders() {
        List<Order> orders = orderDAO.findAll();
        if (orders == null)
            throw new NoSuchElementException("There are no orders in the table");
        return orders;
    }

    public void addOrder(List<String> values, OrderGUI gui) throws SQLException {
        ClientBLL c = new ClientBLL();
        Client client = c.findClientBy("id", values.get(0));
        ProductBLL p = new ProductBLL();
        Product product = p.findProductBy("id", values.get(2));
        if (client == null)
            throw new NoSuchElementException("The client with id = " + values.get(0) + " was not found!");
        if (product == null)
            throw new NoSuchElementException("The product with id = " + values.get(2) + " was not found!");
        if (Integer.parseInt(values.get(4)) <= product.getStock()) {
            product.updateStock(Integer.parseInt(values.get(4)));
            List<String> fields = new ArrayList<String>();
            List<String> valori = new ArrayList<String>();
            fields.add("stock");
            valori.add(product.getStock() +"");
            p.editProduct(fields, valori, product.getId()+"");

            Order auxiliar = new Order(Integer.parseInt(values.get(0)), values.get(1), Integer.parseInt(values.get(2)), values.get(3), Integer.parseInt(values.get(4)));
            validators.get(0).validate(auxiliar);
            orderDAO.insert(values);

            List<Order> allOrders = orderDAO.findAll();
            Order orderAuxiliar = new Order();
            for(Order order: allOrders)
                orderAuxiliar=order;

            BillBLL b = new BillBLL();
            List<String> billValues = new ArrayList<String>();
            billValues.add(orderAuxiliar.getId() + "");
            billValues.add(Integer.parseInt(values.get(4))* product.getPrice() + "");
            b.addBill(billValues);

            System.out.println("Bill of order(" + orderAuxiliar.getId() + ") has been generated and its total price is: " + Integer.parseInt(values.get(4))* product.getPrice());
        }
        else {
            JOptionPane.showMessageDialog(gui, "We don't have the desired quantity at this moment");
        }
    }
}
