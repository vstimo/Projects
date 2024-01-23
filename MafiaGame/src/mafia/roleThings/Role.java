package mafia.roleThings;

import java.io.Serializable;
/**
 * class for storing the roles of the players (client side)
 */

public interface Role extends Serializable {
    /**
     * @return the role as a string
     */
    public String getRoleString();
}
