package businessLogic.validators;
import model.Order;

public class QuantityValidator implements Validator<Order> {
    public void validate(Order o) {
        if (o.getQuantity() < 0) throw new IllegalArgumentException("Quantity must be a positive number");
    }
}
