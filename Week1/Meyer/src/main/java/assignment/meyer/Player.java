package assignment.meyer;

import org.apache.commons.lang3.EnumUtils;

/**
 *
 * @author Henning
 */
public class Player {

    private int life = 6;
    private String name;
    private String playerType;

    public Player(String name) {
        this.life = 6;
        this.name = name;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayerType() {
        return playerType;
    }

    public void setPlayerType(String playerType) {
        if (EnumUtils.isValidEnum(PlayerTypes.class, playerType.toUpperCase())) {
            this.playerType = playerType;
        } else {
            throw new IllegalArgumentException("Invalid playertype");
        }
    }

    public enum PlayerTypes {
        HUMAN, COMPUTER 
    }
}
