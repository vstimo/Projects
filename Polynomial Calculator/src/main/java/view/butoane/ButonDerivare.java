package view.butoane;

import model.Operatii;
import model.Polinom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.*;
public class ButonDerivare implements ActionListener {
    private final JTextField textField1;
    private final JLabel label;

    public ButonDerivare(JLabel l, JTextField t1) {
        this.label=l;
        this.textField1=t1;
    }

    public void actionPerformed(ActionEvent e) {
        Polinom p1 = new Polinom(textField1.getText() + "");
        Polinom p3 = new Polinom();

        Map<Integer, Integer> polinom1=p1.getPolinom();
        Operatii derivare = new Operatii();
        Map<Integer, Integer> polinom3=derivare.derivare(polinom1);
        label.setText(p3.polToString(polinom3)+"");
    }
}

