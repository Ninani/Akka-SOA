package pl.edu.agh.game.message.environment;

public class ActionMessage {

    private int position;

    public ActionMessage() {
    }

    public ActionMessage(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
