package pl.edu.agh.game.message.environment;

import akka.actor.ActorRef;

public class LocationMessage {

    private int currentPosition;
    private ActorRef enemyAgent;

    public LocationMessage(int currentPosition){
        this.currentPosition = currentPosition;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public ActorRef getEnemyAgent() {
        return enemyAgent;
    }
}
