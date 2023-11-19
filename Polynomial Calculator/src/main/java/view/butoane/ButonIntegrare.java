package view.butoane;

import model.Operatii;
import model.Polinom;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ButonIntegrare implements ActionListener {
    private final JTextField textField1;
    private JLabel label;

    public ButonIntegrare(JLabel l, JTextField t1) {
        this.label=l;
        this.textField1=t1;
    }

    public void actionPerformed(ActionEvent e) {
        Polinom p1 = new Polinom(textField1.getText() + "");
        Polinom p3 = new Polinom();

        Map<Integer, Integer> polinom1=p1.getPolinom();
        Operatii integrare = new Operatii();
        Map<Integer, Double> polinom3=integrare.integrare(polinom1);
        label.setText(p3.polToStringD(polinom3)+"");
    }
}
