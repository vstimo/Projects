package mafia.custom;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton {
    public CustomButton(String text, Color color, Color fontColor) {
        super(text);
        setForeground(fontColor);
        setBackground(color);
        setPreferredSize(new Dimension(100, 50));
        setFont(new Font("Papyrus", Font.BOLD, 16));
        setFocusPainted(false);
        setContentAreaFilled(true);
    }
}
