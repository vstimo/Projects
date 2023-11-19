package businessLogic.validators;
import model.Client;

public class NameValidator implements Validator<Client> {
    public void validate(Client c) {
        for (int i = 0; i < c.getName().length(); i++)
            if (c.getName().charAt(i) >= '0' && c.getName().charAt(i) <= '9')
                throw new IllegalArgumentException("Name most not have digits in it");
    }
}