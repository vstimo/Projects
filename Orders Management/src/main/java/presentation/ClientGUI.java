package presentation;
import businessLogic.ClientBLL;
import model.Client;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientGUI extends JFrame implements ActionListener {
    private JLabel idLabel, nameLabel, addressLabel, emailLabel, ageLabel;
    private JTextField idField, nameField, addressField, emailField, ageField;
    private JButton insertButton, updateButton, deleteButton, selectAllButton;
    private JTable clientTable;
    private JScrollPane scrollPane;
    private ClientBLL clientBLL;
    private DefaultTableModel tableModel;

    public ClientGUI() {
        super("Client Table");
        clientBLL = new ClientBLL();

        //Cream componenetele necesare pentru interfata grafica
        idLabel = new JLabel("ID");
        nameLabel = new JLabel("Name:");
        addressLabel = new JLabel("Address:");
        emailLabel = new JLabel("Email:");
        ageLabel = new JLabel("Age:");
        idField = new JTextField(3);
        nameField = new JTextField(20);
        addressField = new JTextField(20);
        emailField = new JTextField(20);
        ageField = new JTextField(3);
        selectAllButton = new JButton("SELECT ALL CLIENTS");
        insertButton = new JButton("ADD A CLIENT");
        updateButton = new JButton("EDIT A CLIENT");
        deleteButton = new JButton("DELETE A CLIENT");

        selectAllButton.addActionListener(this);
        insertButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);

        tableModel = ReflectionTable.generateTable(new Client());
        clientTable = new JTable(tableModel);
        clientTable.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(clientTable);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(idLabel);
        inputPanel.add(idField);
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(addressLabel);
        inputPanel.add(addressField);
        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(ageLabel);
        inputPanel.add(ageField);

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
            if (e.getActionCommand().equals("SELECT ALL CLIENTS")) {
                List<Object> objects = new ArrayList<>();
                objects.addAll(clientBLL.findAllClients());

                ReflectionTable.populateTable(tableModel, objects);
            }
            else if(e.getActionCommand().equals("ADD A CLIENT")){
                List<String> values = new ArrayList<String>();
                values.add(nameField.getText()); values.add(addressField.getText());
                values.add(emailField.getText()); values.add(ageField.getText());
                clientBLL.addClient(values);
                clearFields();
                List<Object> objects = new ArrayList<>();
                objects.addAll(clientBLL.findAllClients());
                ReflectionTable.populateTable(tableModel, objects);
            }
            else if(e.getActionCommand().equals("EDIT A CLIENT")){
                List<String> values = new ArrayList<String>(), fields = new ArrayList<String>();
                if(!nameField.getText().equals("")) {values.add(nameField.getText()); fields.add("name");}
                if(!addressField.getText().equals("")) {values.add(addressField.getText()); fields.add("adress");}
                if(!emailField.getText().equals("")) {values.add(emailField.getText()); fields.add("email");}
                if(!ageField.getText().equals("")) {values.add(ageField.getText()); fields.add("age");}
                clientBLL.editClient(fields, values, idField.getText());
                clearFields();
                List<Object> objects = new ArrayList<>();
                objects.addAll(clientBLL.findAllClients());
                ReflectionTable.populateTable(tableModel, objects);
            }
            else if(e.getActionCommand().equals("DELETE A CLIENT")){
                 String field, value;
                 if(!idField.getText().equals("")) {field="ID"; value=idField.getText();}
                 else if(!nameField.getText().equals("")) {field="name"; value=nameField.getText();}
                 else if(!addressField.getText().equals("")) {field="adress"; value=addressField.getText();}
                 else if(!emailField.getText().equals("")) {field="email"; value=emailField.getText();}
                 else if(!ageField.getText().equals("")) {field="age"; value=ageField.getText();}
                 else {field=""; value="";}
                 clientBLL.deleteClient(field, value);
                clearFields();
                List<Object> objects = new ArrayList<>();
                objects.addAll(clientBLL.findAllClients());
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
        addressField.setText("");
        emailField.setText("");
        ageField.setText("");
    }
}