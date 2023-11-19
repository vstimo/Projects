package presentation;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Clasa utilizata cu un singur scop major si anume generarea antetului tabelului precum si popularea acestuia intr-un mod cat mai generic
 */
public class ReflectionTable {
    public static DefaultTableModel generateTable(Object object) { //metoda pentru a crea antetul tabelului
        int nr = object.getClass().getDeclaredFields().length;
        String[] fieldList = new String[nr];
        nr = 0;

        for (Field field : object.getClass().getDeclaredFields()) {
            fieldList[nr] = field.getName();
            nr++;
        }
        return new DefaultTableModel(fieldList, 0);
    }

    public static void populateTable(DefaultTableModel tableModel, List<Object> objects) { //metoda pentru a popula tabelul
        tableModel.setRowCount(0); //clear table
        for (Object object : objects) {
            int nr = object.getClass().getDeclaredFields().length;
            int i = 0;
            Object[] row = new Object[nr];
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    row[i] = field.get(object);
                    i++;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            tableModel.addRow(row);
        }
    }
}
