package mafia.roleThings.goodGuys;

import mafia.roleThings.Role;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * class for the role , NormalCitizen
 */
public class NormalCitizen implements Role {
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private String roleNameString;
    public NormalCitizen(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.roleNameString = "normal citizen";
    }

    @Override
    public String getRoleString() {
        return roleNameString;
    }
}
