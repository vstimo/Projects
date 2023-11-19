import presentation.ClientGUI;
import presentation.OrderGUI;
import presentation.ProductGUI;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        new ClientGUI();
        new ProductGUI();
        new OrderGUI();
    }
}