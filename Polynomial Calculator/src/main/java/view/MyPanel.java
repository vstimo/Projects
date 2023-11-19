package view;

import view.butoane.*;

import javax.swing.*;
import java.awt.*;
public class MyPanel extends JPanel {
    JFrame frame;
    JPanel panel1,panel2,panel3,panel4,p;
    JLabel l1, l2, l3, l4;
    JTextField tf1,tf2;
    JButton b1,b2,b3,b4,b5,b6;
    public MyPanel() {
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        ////PRIMUL PANEL -> pt primul polinom
        l1 = new JLabel("Polinom1: ");
        tf1 = new JTextField(20);
        panel1.add(l1);
        panel1.add(tf1);
        panel1.setLayout(new FlowLayout());
        ////AL DOILEA PANEL -> pt al doilea polinom
        l2 = new JLabel("Polinom2: ");
        tf2 = new JTextField(20);
        panel2.add(l2);
        panel2.add(tf2);
        panel2.setLayout(new FlowLayout());
        ////AL TREILEA PANEL -> pt rezultat
        l3 = new JLabel("Rezultat: ");
        l4 = new JLabel("");
        panel3.add(l3);
        panel3.add(l4);
        panel3.setLayout(new FlowLayout());
        ////AL PATRULEA PANEL -> pt butoane
        b1 = new JButton("+");
        b1.addActionListener(new ButonAdunare(l4,tf1,tf2));
        b2 = new JButton("-");
        b2.addActionListener(new ButonScadere(l4,tf1,tf2));
        b3 = new JButton("Derivare");
        b3.addActionListener(new ButonDerivare(l4,tf1));
        b4 = new JButton("Integrare");
        b4.addActionListener(new ButonIntegrare(l4,tf1));
        b5 = new JButton("*");
        b5.addActionListener(new ButonInmultire(l4,tf1,tf2));
        b6 = new JButton("/");
        b6.addActionListener(new ButonImpartire(l4,tf1,tf2));
        panel4.add(b1);
        panel4.add(b2);
        panel4.add(b3);
        panel4.add(b4);
        panel4.add(b5);
        panel4.add(b6);
        panel4.setLayout(new FlowLayout());

        p = new JPanel();
        p.add(panel1);
        p.add(panel2);
        p.add(panel3);
        p.add(panel4);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        frame.setContentPane(p);
        frame.setVisible(true);
    }
}