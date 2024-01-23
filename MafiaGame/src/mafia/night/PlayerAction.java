package mafia.night;

import java.io.Serializable;
/**
 * a class for actions of the players
 */
public class PlayerAction implements Serializable {
    private PlayersActionTypes PlayerActionType;
    private String nameOfThePlayerActionHappensTo;

    public PlayerAction(PlayersActionTypes PlayerActionType, String nameOfThePlayerActionHappensTo) {
        this.PlayerActionType = PlayerActionType;
        this.nameOfThePlayerActionHappensTo = nameOfThePlayerActionHappensTo;
    }

    public PlayersActionTypes getPlayerActionType() {
        return PlayerActionType;
    }

    public String getNameOfThePlayerActionHappensTo() {
        return nameOfThePlayerActionHappensTo;
    }
}
