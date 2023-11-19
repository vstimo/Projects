package businessLogic.validators;
import model.Product;

public class StockValidator implements Validator<Product> {
    public void validate(Product p) {
        if (p.getStock() < 0) throw new IllegalArgumentException("Stock must be a positive number");
    }
}
