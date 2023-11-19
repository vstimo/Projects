package dao;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import connection.ConnectionFactory;

/**
 * Aceasta clasa a fost realizata folosind tehnica reflexiei. Cu ajutorul acestei tehnici am scurtat din liniile de cod scrise si chiar daca
 * codul nu ofera o lizibilitate mai mare in aceasta forma, evitam totodata si aparitia de erori nedorite.
 * Deoarece aproape fiecare dintre tabele prezinta operatii de afisare, adaugare, insereare si stergere aici am dezvoltat metodele necesare
 * in mod generic.
 * Metoda findBy: primeste un camp si o valoare, presupunand ca se primeste o singura conditie
 * Metoda findAll:
 * Metoda insert: primeste o lista de valori deoarece se insereaza  o valoare in fiecare camp a tabelului, mai putin pe coloana id deoarece
 * aceasta coloana a fost setata auto-incremental
 * Metoda update: primeste o lista de campuri, o lista de valori care corespunde acestor campuri si un id. Actualizarea se face pentru obiectul
 * cu id-ul primit ca si parametru
 * Metoda delete: primeste un camp si o valoare si sterge randurile care respecta conditia formata din campul si valoarea respectiva
 * Metoda createObject: preluata din exemplul de implementare
 * @param <T>
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public T findBy(String field, String value) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuilder query = new StringBuilder();
        /* Daca dorim sa gasim un rand din tabel in functie de un String atunci pentru a se respecta sintaxa de MySQL trebuie adaugate
        apostroafe, altfel daca conditia depinde doar de un numar nu trebuie adaugate apostroafe.
         */
        if (!(value.charAt(0) >= 0 && value.charAt(0) <= 9))
            query.append("SELECT * FROM warehousedb." + type.getSimpleName() + " WHERE " + field + " = '" + value + "'");
        else query.append("SELECT * FROM warehousedb." + type.getSimpleName() + " WHERE " + field + " = " + value);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query.toString());
            resultSet = statement.executeQuery();
            try{
            if (!type.getSimpleName().equals("Bill")) return createObjects(resultSet).get(0);
            else {
                return (T) new Object(); //caz special in caz ca avem un obiect Bill, vezi Documentatie
            }
            } catch (IndexOutOfBoundsException e){
                throw new IndexOutOfBoundsException("The following " + type.getSimpleName() + " with id = " + value + " was not found in data base");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findBy " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuilder query = new StringBuilder();
        query.append("SELECT * from warehousedb." + type.getSimpleName());
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query.toString());
            resultSet = statement.executeQuery();
            if (!type.getSimpleName().equals("Bill")) return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public void insert(List<String> values) throws SQLException {
        String tableName = type.getSimpleName();
        //Salvam intr-o lista de string-uri numele coloanelor din tabel care corespund tuturor campurilor CARE NU SUNT ID ale obiectului inserat
        List<String> columns = Arrays.stream(type.getDeclaredFields())
                .filter(field -> !field.getName().equals("id"))
                .map(Field::getName)
                .collect(Collectors.toList());

        StringBuilder sql = new StringBuilder(); //construim statement-ul
        sql.append("INSERT INTO warehousedb.").append(tableName).append(" (");
        sql.append(String.join(", ", columns)).append(") VALUES (");
        sql.append(String.join(", ", Collections.nCopies(columns.size(), "?"))).append(")");

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < values.size(); i++) {
                statement.setString(i + 1, values.get(i)); //in locurile unde sunt ? se pun valori
            }
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted != 1) {
                throw new SQLException("Expected to insert 1 row, but inserted " + rowsInserted);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, tableName + "DAO:insert " + e.getMessage());
            throw e;
        }
    }

    public void update(List<String> fields, List<String> values, int id) throws SQLException {
        String tableName = type.getSimpleName();
        //Salvam intr-o lista de string-uri numele coloanelor din tabel care corespund tuturor campurilor CARE NU SUNT ID ale obiectului inserat
        List<String> setClauses = IntStream.range(0, fields.size())
                .mapToObj(i -> fields.get(i) + " = ?")
                .collect(Collectors.toList());
        //Construim statement-ul
        String sql = "UPDATE warehousedb." + tableName + " SET " + String.join(", ", setClauses) + " WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                statement.setString(i + 1, values.get(i)); //in locurile unde sunt ? se pun valori
            }
            statement.setInt(fields.size() + 1, id); //in locurile unde sunt ? se pun valori

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated != 1) {
                throw new SQLException("Expected to update 1 row, but updated " + rowsUpdated);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, tableName + "DAO:update " + e.getMessage());
            throw e;
        }
    }

    public void delete(String field, String value) throws SQLException {
        String tableName = type.getSimpleName();
        String sql; //construim statement-ul
        if (!(value.charAt(0) >= 0 && value.charAt(0) <= 9))
            sql = "DELETE FROM warehousedb." + tableName + " WHERE " + field + " = '" + value + "'";
        else sql = "DELETE FROM warehousedb." + tableName + " WHERE " + field + " = " + value;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                LOGGER.warning("No rows deleted from " + tableName + " where " + field + " = " + value);
            } else if (rowsDeleted > 1) {
                LOGGER.warning("Deleted " + rowsDeleted + " rows from " + tableName + " where " + field + " = " + value);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, tableName + "DAO:delete " + e.getMessage());
            throw e;
        }
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T) ctor.newInstance();

                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }
}
