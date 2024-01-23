package mafia.night;

import java.io.Serializable;
/**
 * an enum for the types of the actions of the player
 */
public enum PlayersActionTypes implements Serializable {

    killerVictim,
    townDoctorSave,
    detect,
    mute,
    mayorCancelsTheVoting,
}
