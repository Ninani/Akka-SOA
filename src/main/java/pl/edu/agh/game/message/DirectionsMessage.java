package pl.edu.agh.game.message;

public class DirectionsMessage {

    private int position;

    public DirectionsMessage() {
    }

    public DirectionsMessage(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
