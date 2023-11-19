package businessLogic;
import businessLogic.validators.StockValidator;
import businessLogic.validators.Validator;
import dao.ProductDAO;
import model.Product;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import static java.lang.Integer.parseInt;

/**
 * Aceasta clasa contine logica din spate. Folosind metodele definite in ProductDAO se creaza noi metode cu functionalitati similare conceptului
 * de OOP dar care au la baza teorie de MySQL si care sunt mai departe utilizate in clasele pentru GUI
 */
public class ProductBLL{
    private List<Validator<Product>> validators;
    private ProductDAO productDAO;

    public ProductBLL(){
        validators = new ArrayList<Validator<Product>>();//nu avem validatori pentru ID, nume si pret deoarece nu s-a considerat necesar
        validators.add(new StockValidator());
        productDAO = new ProductDAO();
    }

    public Product findProductBy(String field, String value){
        Product product = productDAO.findBy(field, value);
        if(product == null)
            throw new NoSuchElementException("The product with id = " + value + " was not found!");
        return product;
    }

    public List<Product> findAllProducts(){
        List<Product> products= productDAO.findAll();
        if(products == null)
            throw new NoSuchElementException("There are no products in the table");
        return products;
    }

    public void addProduct(List<String> values) throws SQLException {
        if(values.size()!=3)
            throw new IllegalArgumentException("There are some fields with no data");
        Product auxiliar = new Product(values.get(0), parseInt(values.get(1)), parseInt(values.get(2)));
        validators.get(0).validate(auxiliar);
        productDAO.insert(values);
    }

    public void editProduct(List<String> fields, List<String> values, String id) throws SQLException {
        Product aux = productDAO.findBy("id",id);
        if(aux == null)
            throw new NoSuchElementException("The product with id = " + id + " was not found!");
        for(int i=0;i<fields.size();i++){
            if(fields.get(i).equals("name")) aux.setName(values.get(i));
            if(fields.get(i).equals("stock")) aux.setStock(Integer.parseInt(values.get(i)));
            if(fields.get(i).equals("price")) aux.setPrice(Integer.parseInt(values.get(i)));
        }
        validators.get(0).validate(aux);
        productDAO.update(fields, values, Integer.parseInt(id));
    }

    public void deleteProduct(String field, String value) throws SQLException {
        Product product = productDAO.findBy(field, value);
        if(product == null)
            throw new NoSuchElementException("The product with id = " + value + " was not found!");
        productDAO.delete(field, value);
    }
}
