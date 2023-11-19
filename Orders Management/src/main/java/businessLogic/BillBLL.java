package businessLogic;
import dao.BillDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import model.Order;
import model.Product;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Aceasta clasa contine logica din spate. Folosind metodele definite in BillDAO se creaza noi metode cu functionalitati similare conceptului
 * de OOP dar care au la baza teorie de MySQL si care sunt mai departe utilizate in clasele pentru GUI
 */
public class BillBLL {
    private BillDAO billDAO;

    public BillBLL() {
        billDAO = new BillDAO();
    }

    public String findBillById(String value){
        if(billDAO.findBy("idOrder", value) == null)
            throw new NoSuchElementException("The bill with id = " + value + " was not found!");
        OrderDAO o = new OrderDAO();
        Order order = o.findBy("id",value);
        ProductDAO p = new ProductDAO();
        Product product = p.findBy("id",order.getIdProduct()+ "");
        return "The bill with id("+value+") has a total price of " + product.getPrice()*Integer.parseInt(order.getQuantity()+"") + " $";
    }

    public void addBill(List<String> values) throws SQLException {
        billDAO.insert(values);
    }
}
