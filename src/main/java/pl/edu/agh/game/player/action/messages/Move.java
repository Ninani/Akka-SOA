package pl.edu.agh.game.player.action.messages;

import pl.edu.agh.game.model.map.Direction;

public class Move extends ActionMsg {
    private Direction direction;

    public Move(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
