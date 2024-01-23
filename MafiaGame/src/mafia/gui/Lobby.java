package mafia.gui;

import mafia.custom.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Lobby extends JFrame {
    private static final Color KAKI = new Color(174, 194, 198);
    public CustomButton submitButton, readyButton;
    private JTextField nameField;
    private String playerName = "";
    public JLabel statusPlayerLabel, instructionLabel;
    private List<ButtonClickListener> listeners = new ArrayList<>();

    public Lobby() {
        generateGraphicalInterface();
        buttonsManage();
    }

    private void generateGraphicalInterface() {
        getContentPane().setBackground(KAKI);
        setTitle("Mafia");
        setSize(650, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());// Setting layout manager

        /*
        T O P    P A N E L
        Contains a label with the title "Welcome to Mafia" which is positioned in the north part
        of the frame.
         */
        JLabel titleLabel = new JLabel("WELCOME TO MAFIA!");
        titleLabel.setFont(new Font("Finger Printed", Font.PLAIN, 40));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setPreferredSize(new Dimension(520, 100));
        add(titleLabel, BorderLayout.NORTH);

        /*
        L E F T   P A N E L
        Contains a label which indicates the area to type user's name.
        A text area where the user can type his desired name for the current game.
        Two button: -> Submit button will be pressed when user is decided regarding his name
                    -> Ready button will be pressed rather user is ready/ or not ready to enter the game
         */
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(KAKI);
        JLabel nameLabel = new JLabel("Enter your name: ");
        nameLabel.setFont(new Font("Papyrus", Font.PLAIN, 25));
        leftPanel.add(nameLabel);

        nameField = new JTextField(11);
        nameField.setPreferredSize(new Dimension(20, 25));
        nameField.setFont(new Font("Roboto", Font.PLAIN, 20));
        leftPanel.add(nameField);

        submitButton = new CustomButton("Submit", new Color(227, 221, 211), Color.DARK_GRAY);
        readyButton = new CustomButton("Ready", new Color(37, 43, 53), Color.WHITE);

        leftPanel.add(submitButton);
        leftPanel.add(readyButton);
        leftPanel.setPreferredSize(new Dimension(250, 150));

        /*
        R I G H T    P A N E L
        Contains a label called "Players".
        It also contains an array of JLabels with calls the method getListOfPlayers() from the server and then
        it shows on the screen.
         */
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(KAKI);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        statusPlayerLabel = new JLabel();
        statusPlayerLabel.setFont(new Font("Impact", Font.PLAIN, 25));

        instructionLabel = new JLabel();
        instructionLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));

        rightPanel.add(statusPlayerLabel);
        rightPanel.add(instructionLabel);

        rightPanel.setPreferredSize(new Dimension(285,100));

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        setVisible(true);
    }

    public void setStatusPlayerLabel(String status, Color color){
        this.statusPlayerLabel.setText(status);
        this.statusPlayerLabel.setForeground(color);
    }

    public void setInstructionLabel(String instruction, Color color){
        this.instructionLabel.setText(instruction);
        this.instructionLabel.setForeground(color);
    }

    public void addButtonClickListener(ButtonClickListener listener){
        listeners.add(listener);
    }
    private void buttonsManage() {
        submitButton.addActionListener(e -> {
            playerName = nameField.getText();
            for (ButtonClickListener listener : listeners)
                listener.submitButtonClicked();
        });

        readyButton.addActionListener(e -> {
            if (!submitButton.isEnabled())
                for (ButtonClickListener listener : listeners)
                    listener.readyButtonClicked();

            //AICI TREBUIE SA MODIFIC CA SA NU MAI IA NUMELE READY CREEEEED!!!
        });
    }

    public String getPlayerName(){
        return playerName;
    }
}