package businessLogic.validators;

/**
 * Interfata implementata de clasele: EmailValidator, NameValidator, QuantityValidator si StockValidator pentru asigurarea introducerii unor
 * date valide pentru baza de date
 * @param <T>
 */
public interface Validator<T> {
    public void validate(T t);
}
