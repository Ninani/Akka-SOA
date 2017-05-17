package pl.edu.agh.game.player.action.messages;

import pl.edu.agh.game.model.map.Direction;

import java.io.Serializable;

public class Turn implements Serializable {
    public Direction direction;

    public Turn(Direction direction) {
        this.direction = direction;
    }
}
