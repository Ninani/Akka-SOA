package pl.edu.agh.game.message.environment;


import akka.actor.ActorRef;
import pl.edu.agh.game.enemy.EnemyAgent;
import pl.edu.agh.game.model.map.Direction;

public class MoveMessage {

    private int currentPosition;
    private Direction direction;
    private ActorRef enemyAgent;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public ActorRef getEnemyAgent() {
        return enemyAgent;
    }

    public void setEnemyAgent(ActorRef enemyAgent) {
        this.enemyAgent = enemyAgent;
    }
}
