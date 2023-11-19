package GUI;
import javax.swing.*;
public class MyPanel extends JPanel{
    JFrame frame;
    JPanel panel1, panel2, panel3, panel4, panel5, panel6 ,panel7, p;
    JLabel l1, l2, l3, l4, l5;
    JTextField tf1, tf2, tf3, tf4, tf5;
    JRadioButton r1, r2;
    ButtonGroup radioButtonGroup;
    JButton b1;
    public MyPanel(){
        frame = new JFrame("Queue Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 480);

        panel1 = new JPanel(); panel2 = new JPanel(); panel3 = new JPanel();
        panel4 = new JPanel(); panel5 = new JPanel(); panel6 = new JPanel();
        panel7 = new JPanel();

        //PANEL I -> numarul de clienti
        l1 = new JLabel("Number of clients ");
        tf1 = new JTextField(5);
        panel1.add(l1); panel1.add(tf1);
        //PANEL II -> numarul de queues
        l2 = new JLabel("Number of queue ");
        tf2 = new JTextField(5);
        panel2.add(l2); panel2.add(tf2);
        //PANEL III -> intervalul de simulare
        l3 = new JLabel("Simulation interval ");
        tf3 = new JTextField(5);
        panel3.add(l3); panel3.add(tf3);
        //PANEL IV -> intervalul de sosire
        l4 = new JLabel("Arrival time interval ");
        tf4 = new JTextField(5);
        panel4.add(l4); panel4.add(tf4);
        //PANEL V -> intervalul de servire
        l5 = new JLabel("Service time interval ");
        tf5 = new JTextField(5);
        panel5.add(l5); panel5.add(tf5);
        //PANEL VI -> pentru a selecta shortest_queue sau shortest_time
        r1 = new JRadioButton("shortest time");
        r2 = new JRadioButton("shortest queue");
        radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(r1);
        radioButtonGroup.add(r2);
        panel6.add(r1);
        panel6.add(r2);
        //PANEL VII -> buton de start
        b1=new JButton("START");
        ButonStart start = new ButonStart(tf1,tf2,tf3,tf4,tf5,l1,l2,l3,l4,l5,r1,r2,frame);
        b1.addActionListener(start);
        panel7.add(b1);

        p= new JPanel();
        p.add(panel1); p.add(panel2); p.add(panel3); p.add(panel4); p.add(panel5); p.add(panel6); p.add(panel7);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        frame.setContentPane(p);
        frame.setVisible(true);
    }
}
