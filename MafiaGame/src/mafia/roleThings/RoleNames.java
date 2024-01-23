package mafia.roleThings;

import java.io.Serializable;

/**
 * enum for storing the roles of the players ( server side )
 */
public enum RoleNames implements Serializable {
    // mafias :
    killer,
    // people :
    mayor,
    normalCitizen,
    therapist,
    townDoctor,
    detective;

    public static String getRoleAsString(RoleNames roleName){
        if(roleName == killer){
            return "killer";
        }
        else if(roleName == mayor){
            return "mayor";
        }
        else if(roleName == normalCitizen){
            return "normal citizen";
        }
        else if(roleName == therapist){
            return "therapist";
        }
        else if(roleName == townDoctor){
            return "town doctor";
        }
        else if(roleName == detective){
            return "detective";
        }
        else {
            return null;
        }
    }

    public static boolean isEvil(RoleNames roleName){
        if(roleName == killer) return true;
        return false;
    }
}
