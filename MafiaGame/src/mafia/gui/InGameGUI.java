package mafia.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.util.ArrayList;
import java.util.Collections;

public class InGameGUI extends JFrame {
    public JPanel miniWindowsPanel;
    public JPanel timerPanel;
    public JLabel timerLabel;
    public JLabel gameTimeLabel;
    public JTextField textField;
    public int secondsRemaining;
    public Timer timer;
    private static final Color DAY = new Color(136, 202, 252), NIGHT = new Color(2, 41, 125);
    public JLabel imageLabel;
    public JTextArea chatArea;
    private java.util.List<TextSenderListener> listeners = new ArrayList<>();

    public InGameGUI() {
        setTitle("Mafia");
        setSize(700, 830);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout()); // Main layout of the game

        // Create a timer panel
        timerPanel = new JPanel(new BorderLayout());
        timerLabel = new JLabel("180 s ");
        timerPanel.add(timerLabel, BorderLayout.EAST);

        gameTimeLabel = new JLabel(" ");
        gameTimeLabel.setHorizontalAlignment(JLabel.CENTER);
        gameTimeLabel.setFont(new Font("IMPACT", Font.PLAIN, 25));
        timerPanel.add(gameTimeLabel, BorderLayout.CENTER);

        miniWindowsPanel = new JPanel(new GridLayout(4, 4, 5, 5)); // Create a panel for the mini windows
        miniWindowsPanel.setPreferredSize(new Dimension(600, 495));

        // Create 16 mini windows
        for (int i = 0; i < 16; i++) {
            JPanel miniWindow = new JPanel();
            miniWindow.setBackground(NIGHT);
            miniWindowsPanel.add(miniWindow);
        }

        JPanel textAreaPanel = new JPanel(new BorderLayout());
        textField = new JTextField();
        textField.setFont(new Font("Berlin Sans FB", Font.PLAIN, 25));
        textField.setPreferredSize(new Dimension(600, 30));

        // Create a chat text area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Berlin Sans FB", Font.PLAIN, 30));

        JScrollPane scroll = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension(600, 180));

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (TextSenderListener listener : listeners)
                    listener.sendText();
                textField.setText("");
            }
        });
        textAreaPanel.add(textField, BorderLayout.NORTH);
        textAreaPanel.add(scroll, BorderLayout.CENTER);

        // Add the components to the main panel
        add(timerPanel, BorderLayout.NORTH);
        add(miniWindowsPanel, BorderLayout.CENTER);
        add(textAreaPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void addTextSenderListener(TextSenderListener listener){
            listeners.add(listener);}

    public void setCountDownTimer(int time) {
        if (timer != null) timer.stop();
        secondsRemaining = time;

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsRemaining--;
                timerLabel.setText(secondsRemaining + " s ");

                if (secondsRemaining <= 0) {
                    timer.stop();
                    // Perform actions when the timer reaches 0 (e.g., end the game)
                }
            }
        });
        timer.start();
    }

    public void setDay() {
        Component[] components = miniWindowsPanel.getComponents();
        for (Component component : components)
            component.setBackground(DAY);

        if(imageLabel!=null) timerPanel.remove(imageLabel);
        ImageIcon sunIcon = new ImageIcon(getClass().getResource("resources/sun.png"));
        Image sunImage = sunIcon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        sunIcon = new ImageIcon(sunImage);

        imageLabel = new JLabel(sunIcon);
        timerPanel.add(imageLabel, BorderLayout.WEST);
    }

    public void setNight() {
        Component[] components = miniWindowsPanel.getComponents();
        for (Component component : components)
            component.setBackground(NIGHT);

        if(imageLabel!=null) timerPanel.remove(imageLabel);
        ImageIcon moonIcon = new ImageIcon(getClass().getResource("resources/moon.png"));
        Image moonImage = moonIcon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        moonIcon = new ImageIcon(moonImage);

        imageLabel = new JLabel(moonIcon);
        timerPanel.add(imageLabel, BorderLayout.WEST);
    }

    public void setPlayersPosition(ArrayList<String> playerNames) {
        ImageIcon playerIcon = new ImageIcon(getClass().getResource("resources/player.png"));
        Image scaledPlayerImage = playerIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        playerIcon = new ImageIcon(scaledPlayerImage);

        JPanel newMiniWindowsPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        newMiniWindowsPanel.setPreferredSize(new Dimension(600, 495));

        ArrayList<Color> playerColors = generateColorsForPlayers();
        Collections.shuffle(playerColors);

        for (int i = 0; i < 16; i++) {
            JPanel miniWindow = new JPanel();
            miniWindow.setBackground(DAY);
            newMiniWindowsPanel.add(miniWindow);

            miniWindow.setLayout(new BorderLayout());
            miniWindow.removeAll();

            if(i < playerNames.size()) {
            ImageIcon auxIcon = applyColorFilter(playerIcon, playerColors.get(i));
            JLabel image = new JLabel(auxIcon);

            miniWindow.add(image, BorderLayout.CENTER);

                JLabel nameLabel = new JLabel(playerNames.get(i));
                nameLabel.setHorizontalAlignment(JLabel.CENTER);
                nameLabel.setForeground(Color.GRAY);
                miniWindow.add(nameLabel, BorderLayout.NORTH);
            }
        }

        // Remove the existing miniWindowsPanel and add the new one
        remove(miniWindowsPanel);
        miniWindowsPanel = newMiniWindowsPanel;
        add(miniWindowsPanel);

        revalidate();
        repaint();
    }

    private ImageIcon applyColorFilter(ImageIcon originalIcon, Color tint) {
        ImageFilter filter = new RGBImageFilter() {
            @Override
            public int filterRGB(int x, int y, int rgb) {
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb) & 0xFF;

                r = (r + tint.getRed()) / 2;
                g = (g + tint.getGreen()) / 2;
                b = (b + tint.getBlue()) / 2;

                return (rgb & 0xFF000000) | (r << 16) | (g << 8) | b;
            }
        };
        ImageProducer ip = new FilteredImageSource(originalIcon.getImage().getSource(), filter);
        Image tintedImage = Toolkit.getDefaultToolkit().createImage(ip);

        return new ImageIcon(tintedImage);
    }

    private ArrayList<Color> generateColorsForPlayers() {
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.WHITE);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.ORANGE);
        colors.add(Color.PINK);
        colors.add(Color.CYAN);
        colors.add(Color.GRAY);
        colors.add(Color.MAGENTA);
        colors.add(Color.RED);
        colors.add(new Color(0, 128, 0));
        colors.add(new Color(255, 165, 0));
        colors.add(new Color(255, 192, 203));
        colors.add(new Color(128, 0, 128));
        colors.add(new Color(0, 128, 128));
        colors.add(new Color(128, 0, 0));
        colors.add(Color.lightGray);
        return colors;
    }

    public void setPopWindow(String message, String title, int messageType) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, message, title, messageType);
        });
    }

    public void setEveryoneSleeps(){
        if(imageLabel!=null) timerPanel.remove(imageLabel);
        ImageIcon moonIcon = new ImageIcon(getClass().getResource("resources/moon.png"));
        Image moonImage = moonIcon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        moonIcon = new ImageIcon(moonImage);

        imageLabel = new JLabel(moonIcon);
        timerPanel.add(imageLabel, BorderLayout.WEST);

        Component[] components = miniWindowsPanel.getComponents();

        for (int i = 0; i < 16; i++) {
            if (components[i] instanceof JPanel) {
                JPanel miniWindow = (JPanel) components[i];
                miniWindow.setLayout(new BorderLayout());
                miniWindow.removeAll();
                miniWindow.setBackground(NIGHT);

                JLabel text = new JLabel("sleeps");
                text.setHorizontalAlignment(JLabel.CENTER);
                text.setVerticalAlignment(JLabel.CENTER);
                text.setFont(new Font("Night Stalker", Font.PLAIN, 50));
                miniWindow.add(text, BorderLayout.CENTER);
            }
        }

        miniWindowsPanel.revalidate();
        miniWindowsPanel.repaint();
    }

    public void setGameTimeLabel(String info){
        this.gameTimeLabel.setText(info);
    }
}