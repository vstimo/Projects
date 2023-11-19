package view.butoane;

import model.Operatii;
import model.Polinom;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ButonInmultire implements ActionListener {
    private final JTextField textField1, textField2;
    private final JLabel label;

    public ButonInmultire(JLabel l, JTextField t1, JTextField t2) {
        this.label=l;
        this.textField1=t1;
        this.textField2=t2;
    }

    public void actionPerformed(ActionEvent e) {
        Polinom p1 = new Polinom(textField1.getText() + "");
        Polinom p2 = new Polinom(textField2.getText() + "");
        Polinom p3 = new Polinom();

        Map<Integer, Integer> polinom1=p1.getPolinom();
        Map<Integer, Integer> polinom2=p2.getPolinom();
        Operatii inmultire = new Operatii();
        Map<Integer, Integer> polinom3=inmultire.inmultire(polinom1,polinom2);
        label.setText(p3.polToString(polinom3)+"");
    }
}