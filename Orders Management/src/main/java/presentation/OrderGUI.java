package presentation;
import businessLogic.BillBLL;
import businessLogic.OrderBLL;
import dao.ClientDAO;
import dao.ProductDAO;
import model.Client;
import model.Order;
import model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderGUI extends JFrame implements ActionListener{
    private JLabel idClientLabel, idProductLabel, quantityLabel, idBillLabel;
    private JTextField idClientField, idProductField, quantityField, idBillField;
    private JButton selectAllButton, insertButton, selectBillById;
    private JTable orderTable;
    private JScrollPane scrollPane;
    private OrderBLL orderBLL;
    private DefaultTableModel tableModel;

    public OrderGUI(){
        super("Order Table");
        orderBLL = new OrderBLL();

        //Cream componentele necesare pentru interfata
        idClientLabel = new JLabel("ID CLIENT: ");
        idProductLabel = new JLabel("ID PRODUCT: ");
        quantityLabel = new JLabel("Quantity: ");
        idBillLabel = new JLabel("ID BILL");
        idClientField = new JTextField(3);
        idProductField = new JTextField(3);
        quantityField = new JTextField(4);
        idBillField = new JTextField(3);
        selectAllButton = new JButton("SELECT ALL ORDERS");
        insertButton = new JButton("ADD AN ORDER");
        selectBillById = new JButton("SELECT BILL BY ID");

        selectAllButton.addActionListener(this);
        insertButton.addActionListener(this);
        selectBillById.addActionListener(this);

        tableModel = ReflectionTable.generateTable(new Order());
        orderTable = new JTable(tableModel);
        orderTable.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(orderTable);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(idClientLabel);
        inputPanel.add(idClientField);
        inputPanel.add(idProductLabel);
        inputPanel.add(idProductField);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField);
        inputPanel.add(idBillLabel);
        inputPanel.add(idBillField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(selectAllButton);
        buttonPanel.add(insertButton);
        buttonPanel.add(selectBillById);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        try{
            if(e.getActionCommand().equals("SELECT ALL ORDERS")){
                List<Object> objects = new ArrayList<>();
                objects.addAll(orderBLL.findAllOrders());

                ReflectionTable.populateTable(tableModel, objects);
            }
            else if(e.getActionCommand().equals("ADD AN ORDER")){
                List<String> values = new ArrayList<String>();
                values.add(idClientField.getText()); //0
                ClientDAO c = new ClientDAO();
                Client client = c.findBy("id",idClientField.getText());
                values.add(client.getName()); //1
                values.add(idProductField.getText()); //2
                ProductDAO p = new ProductDAO();
                Product product  = p.findBy("id",idProductField.getText());
                values.add(product.getName()); //3
                values.add(quantityField.getText()); //4
                orderBLL.addOrder(values,this);
                clearFields();
                List<Object> objects = new ArrayList<>();
                objects.addAll(orderBLL.findAllOrders());
                ReflectionTable.populateTable(tableModel, objects);
            }
            else if(e.getActionCommand().equals("SELECT BILL BY ID")){
                String value = idBillField.getText();
                BillBLL b = new BillBLL();
                String bill = b.findBillById(value);
                if(bill == null)
                    JOptionPane.showMessageDialog(this, "The bill with id = " + value +" was not found in database");
                else JOptionPane.showMessageDialog(this,  bill);
                clearFields();

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error performing database operation", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void clearFields() {
        idClientField.setText("");
        idProductField.setText("");
        quantityField.setText("");
        idBillField.setText("");
    }
}
