package pl.edu.agh.game.player.action.services.action.messages;

import java.io.Serializable;

public class UpdateLifePoints implements Serializable {
    private int lifePoints;

    public UpdateLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public int getLifePoints() {
        return lifePoints;
    }
}
