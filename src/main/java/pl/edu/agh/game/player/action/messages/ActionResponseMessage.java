package pl.edu.agh.game.player.action.messages;

import java.io.Serializable;

public class ActionResponseMessage implements Serializable {
    private final String actionMessage;

    public ActionResponseMessage() {
        this.actionMessage = "response";
    }
}
