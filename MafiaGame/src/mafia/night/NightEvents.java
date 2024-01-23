package mafia.night;

import mafia.serverThings.ServerSidePlayerDetails;

/**
 * a class to store the events in night and conclude from them
 */
public class NightEvents {
    private ServerSidePlayerDetails victim;
    private ServerSidePlayerDetails townDoctorSaves;
    private ServerSidePlayerDetails dieTonight;
    private ServerSidePlayerDetails mutedOne;
    private ServerSidePlayerDetails detectiveWantsToDetect;

    public NightEvents() {
        victim = null;
        dieTonight = null;
        townDoctorSaves = null;
        mutedOne = null;
    }

    /**
     * mafia takes a victim
     * @param victim is the person the mafia member chooses
     */
    public void mafiaTakesVictim(ServerSidePlayerDetails victim) {
        this.victim = victim;
    }

    /**
     * town doctor saves a player
     * @param playerDetails is the saving player
     */
    public void townDoctorSave(ServerSidePlayerDetails playerDetails) {
        townDoctorSaves = playerDetails;
    }

    /**
     * therapist mutes a player
     * @param mutedOne is the muted player
     */
    public void mute(ServerSidePlayerDetails mutedOne) {
        this.mutedOne = mutedOne;
    }

    public ServerSidePlayerDetails getWhoDiesTonight() {
        ServerSidePlayerDetails mafiaVictim = this.victim;
        if (townDoctorSaves != null) {
            if (!townDoctorSaves.equals(mafiaVictim)) {
                dieTonight = mafiaVictim;
            }
        } else dieTonight = mafiaVictim;
        return dieTonight;
    }

    /**
     * resets the events
     */
    public void resetNightEvents() {
        this.dieTonight = null;
        this.townDoctorSaves = null;
        this.victim = null;
        this.mutedOne = null;
        this.detectiveWantsToDetect = null;
    }

    /**
     * @param whoDetectiveWantsToDetect is the player , the detective wants to detect
     */
    public void setWhoDetectiveWantsToDetect(ServerSidePlayerDetails whoDetectiveWantsToDetect) {
        this.detectiveWantsToDetect = whoDetectiveWantsToDetect;
    }

    /**
     * @return the player , the detective wants to detect
     */
    public ServerSidePlayerDetails getWhoDetectiveWantsToDetect() {
        return detectiveWantsToDetect;
    }

    /**
     * @return the muted player
     */
    public ServerSidePlayerDetails getMutedOne() {
        return mutedOne;
    }
}