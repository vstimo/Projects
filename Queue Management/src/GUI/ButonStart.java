package GUI;
import BusinessLogic.SelectionPolicy;
import BusinessLogic.SimulationManager;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ButonStart implements ActionListener {
    public JTextField tf1, tf2, tf3, tf4, tf5;
    public JLabel l1, l2, l3, l4, l5;
    public JRadioButton rd1, rd2;
    public int nrClients, nrQueues, simulationInterval, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime;
    public JFrame frame;
    public ButonStart(JTextField tf1, JTextField tf2, JTextField tf3, JTextField tf4, JTextField tf5, JLabel l1, JLabel l2, JLabel l3, JLabel l4, JLabel l5, JRadioButton rd1, JRadioButton rd2, JFrame frame) {
        this.frame = frame;
        this.tf1 = tf1; this.tf2 = tf2; this.tf3 = tf3; this.tf4 = tf4; this.tf5 = tf5;
        this.l1 = l1; this.l2 = l2; this.l3 = l3; this.l4 = l4; this.l5 = l5;
        this.rd1 = rd1; this.rd2 = rd2;
    }
    //Atunci cand se apasa butonul de Start se salveaza valoriile din GUI in variabilele din program
    public void actionPerformed(ActionEvent e) {
        nrClients = Integer.parseInt(tf1.getText());
        nrQueues = Integer.parseInt(tf2.getText());
        simulationInterval = Integer.parseInt(tf3.getText());
        String arrivalTime = tf4.getText();
        String serviceTime = tf5.getText();
        minArrivalTime = 0; maxArrivalTime = 0; minServiceTime = 0; maxServiceTime = 0;

        SelectionPolicy strategy;
        String pattern = "\\[(\\d+),(\\d+)]"; //utilizam regex pentru a obtin minArrivalTime, maxArrivalTime, minServiceTime si maxServiceTime
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(arrivalTime);
        if (matcher.matches()) {
            minArrivalTime = Integer.parseInt(matcher.group(1));
            maxArrivalTime = Integer.parseInt(matcher.group(2));
        } else {
            System.out.println("No match found at arrivalTime.");
        }

        matcher = regexPattern.matcher(serviceTime);
        if (matcher.matches()) {
            minServiceTime = Integer.parseInt(matcher.group(1));
            maxServiceTime = Integer.parseInt(matcher.group(2));
        } else {
            System.out.println("No match found at serviceTime.");
        }

        if (rd1.isSelected())
            strategy = SelectionPolicy.SHORTEST_TIME;
        else if (rd2.isSelected()) strategy = SelectionPolicy.SHORTEST_QUEUE;
        else strategy = SelectionPolicy.SHORTEST_TIME;

        SimulationManager gen = new SimulationManager(nrClients, nrQueues, simulationInterval, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime, strategy, frame);
        Thread t = new Thread(gen);
        t.start();
    }
}