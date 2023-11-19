package presentation;
import businessLogic.ProductBLL;
import model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductGUI extends JFrame implements ActionListener {
    private JLabel idLabel, nameLabel, stockLabel, priceLabel;
    private JTextField idField, nameField, stockField, priceField;
    private JButton insertButton, updateButton, deleteButton, selectAllButton;
    private JTable productTable;
    private JScrollPane scrollPane;
    private ProductBLL productBLL;
    private DefaultTableModel tableModel;

    public ProductGUI() {
        super("Product Table");
        productBLL = new ProductBLL();

        //Cream componenetele necesare pentru interfata grafica
        idLabel = new JLabel("ID: ");
        nameLabel = new JLabel("Name:");
        stockLabel = new JLabel("Stock:");
        priceLabel = new JLabel("Price:");
        idField = new JTextField(3);
        nameField = new JTextField(20);
        stockField = new JTextField(20);
        priceField = new JTextField(20);
        selectAllButton = new JButton("SELECT ALL PRODUCTS");
        insertButton = new JButton("ADD A PRODUCT");
        updateButton = new JButton("EDIT A PRODUCT");
        deleteButton = new JButton("DELETE A PRODUCT");

        selectAllButton.addActionListener(this);
        insertButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);

        tableModel = ReflectionTable.generateTable(new Product());
        productTable = new JTable(tableModel);
        productTable.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(productTable);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(idLabel);
        inputPanel.add(idField);
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(stockLabel);
        inputPanel.add(stockField);
        inputPanel.add(priceLabel);
        inputPanel.add(priceField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        buttonPanel.add(selectAllButton);
        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand().equals("SELECT ALL PRODUCTS")) {
                List<Object> objects = new ArrayList<>();
                objects.addAll(productBLL.findAllProducts());

                ReflectionTable.populateTable(tableModel, objects);
            }
            else if(e.getActionCommand().equals("ADD A PRODUCT")){
                List<String> values = new ArrayList<String>();
                values.add(nameField.getText()); values.add(stockField.getText());
                values.add(priceField.getText());
                productBLL.addProduct(values);
                clearFields();
                List<Object> objects = new ArrayList<>();
                objects.addAll(productBLL.findAllProducts());
                ReflectionTable.populateTable(tableModel, objects);
            }
            else if(e.getActionCommand().equals("EDIT A PRODUCT")){
                List<String> values = new ArrayList<String>(), fields = new ArrayList<String>();
                if(!nameField.getText().equals("")) {values.add(nameField.getText()); fields.add("name");}
                if(!stockField.getText().equals("")) {values.add(stockField.getText()); fields.add("stock");}
                if(!priceField.getText().equals("")) {values.add(priceField.getText()); fields.add("price");}
                productBLL.editProduct(fields, values, idField.getText());
                clearFields();
                List<Object> objects = new ArrayList<>();
                objects.addAll(productBLL.findAllProducts());
                ReflectionTable.populateTable(tableModel, objects);
            }
            else if(e.getActionCommand().equals("DELETE A PRODUCT")){
                String field, value;
                if(!idField.getText().equals("")) {field="ID"; value=idField.getText();}
                else if(!nameField.getText().equals("")) {field="name"; value=nameField.getText();}
                else if(!stockField.getText().equals("")) {field="stock"; value= stockField.getText();}
                else if(!priceField.getText().equals("")) {field="price"; value=priceField.getText();}
                else {field=""; value="";}
                productBLL.deleteProduct(field, value);
                clearFields();
                List<Object> objects = new ArrayList<>();
                objects.addAll(productBLL.findAllProducts());
                ReflectionTable.populateTable(tableModel, objects);
            }
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error performing database operation", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        stockField.setText("");
        priceField.setText("");
    }
}