package pl.edu.agh.game.player.action.messages;

import java.io.Serializable;

public class ActionResponseMessage implements Serializable {
    public final String actionMessage;

    public ActionResponseMessage(String actionMessage) {
        this.actionMessage = actionMessage;
    }
}
