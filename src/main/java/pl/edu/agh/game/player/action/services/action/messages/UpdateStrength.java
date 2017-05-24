package pl.edu.agh.game.player.action.services.action.messages;

import java.io.Serializable;

public class UpdateStrength implements Serializable {
    private int strength;

    public UpdateStrength(int strength){
        this.strength = strength;
    }

    public int getStrength() {
        return strength;
    }
}
