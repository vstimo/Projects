package mafia.gui;

public interface ButtonClickListenerGame {
    void buttonClickedByKiller();
    void buttonClickedByDetective();
    void buttonClickedByTherapist();
    void buttonClickedByDoctor();
    void buttonClickedByMayor();
    void buttonClickedWhenReady();
    void buttonClickedWhenVoting();
}
