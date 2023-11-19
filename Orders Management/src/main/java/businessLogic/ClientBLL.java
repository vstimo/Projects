package businessLogic;
import businessLogic.validators.EmailValidator;
import businessLogic.validators.NameValidator;
import businessLogic.validators.Validator;
import dao.ClientDAO;
import model.Client;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import static java.lang.Integer.parseInt;

/**
 * Aceasta clasa contine logica din spate. Folosind metodele definite in ClientDAO se creaza noi metode cu functionalitati similare conceptului
 * de OOP dar care au la baza teorie de MySQL si care sunt mai departe utilizate in clasele pentru GUI
 */
public class ClientBLL{
    private List<Validator<Client>> validators;
    private ClientDAO clientDAO;

    public ClientBLL(){
        validators = new ArrayList<Validator<Client>>();//nu am considerat necesar sa avem validatori pentru ID, adresa si varsta
        validators.add(new NameValidator());
        validators.add(new EmailValidator());
        clientDAO = new ClientDAO();
    }

    public Client findClientBy(String field, String value){
        Client client = clientDAO.findBy(field, value);
        if(client == null)
            throw new NoSuchElementException("The client with id = " + value + " was not found!");
        return client;
    }

    public List<Client> findAllClients(){
        List<Client> clients= clientDAO.findAll();
        if(clients == null)
            throw new NoSuchElementException("There are no clients in the table");
        return clients;
    }

    public void addClient(List<String> values) throws SQLException {
        if(values.size()!=4)
            throw new IllegalArgumentException("There are some fields with no data");
        Client auxiliar = new Client(values.get(0), values.get(1), values.get(2), parseInt(values.get(3)));
        validators.get(0).validate(auxiliar);
        validators.get(1).validate(auxiliar);
        clientDAO.insert(values);
    }

    public void editClient(List<String> fields, List<String> values, String id) throws SQLException {
        Client aux = clientDAO.findBy("id",id);
        if(aux == null)
            throw new NoSuchElementException("The client with id = " + id + " was not found!");
        for(int i=0;i<fields.size();i++){
            if(fields.get(i).equals("name")) aux.setName(values.get(i));
            if(fields.get(i).equals("adress")) aux.setAdress(values.get(i));
            if(fields.get(i).equals("email")) aux.setEmail(values.get(i));
            if(fields.get(i).equals("age")) aux.setAge(Integer.parseInt(values.get(i)));
        }
        validators.get(0).validate(aux);
        validators.get(1).validate(aux);
        clientDAO.update(fields, values, Integer.parseInt(id));
    }

    public void deleteClient(String field, String value) throws SQLException {
        Client client = clientDAO.findBy(field, value);
        if(client == null)
            throw new NoSuchElementException("The client with id = " + value + " was not found!");
        clientDAO.delete(field, value);
    }
}
