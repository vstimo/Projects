package mafia.votingThings;

import mafia.gui.ButtonClickListenerGame;
import mafia.custom.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class VoteButtonsGUI extends JFrame {
    private List<ButtonClickListenerGame> listeners = new ArrayList<>();
    public CustomButton pressedButton;

    public VoteButtonsGUI(List<String> options) {
        setTitle("Vote");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(options.size(), 1));

        for (String option : options) {
            CustomButton button = new CustomButton(option, new Color(37, 43, 53), Color.WHITE);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pressedButton = button;
                    for (ButtonClickListenerGame listener : listeners) {
                        listener.buttonClickedByKiller();
                        listener.buttonClickedByDetective();
                        listener.buttonClickedByTherapist();
                        listener.buttonClickedByDoctor();
                        listener.buttonClickedWhenReady();
                        listener.buttonClickedWhenVoting();
                        listener.buttonClickedByMayor();
                    }
                }
            });
            add(button);
        }
        setVisible(true);
    }

    public void addButtonClickListenerGame(ButtonClickListenerGame listener){
        listeners.add(listener);
    }
}