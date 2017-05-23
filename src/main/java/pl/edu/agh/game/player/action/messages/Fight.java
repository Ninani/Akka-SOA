package pl.edu.agh.game.player.action.messages;

import pl.edu.agh.game.model.enemies.beasts.Beast;

import java.io.Serializable;

public class Fight extends ActionMsg {
    private Beast beast;

    public Fight(Beast beast) {
        this.beast = beast;
    }

    public Beast getBeast() {
        return beast;
    }
}

