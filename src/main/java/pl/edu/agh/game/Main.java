package pl.edu.agh.game;

import akka.actor.*;
import pl.edu.agh.game.enemy.EnemyAgent;
import pl.edu.agh.game.environment.EnvironmentAgent;
import pl.edu.agh.game.fight.FightAgent;
import pl.edu.agh.game.model.map.Direction;
import pl.edu.agh.game.player.action.PlayerActionAgent;
import pl.edu.agh.game.player.action.messages.Turn;

public class Main{

    public static void main(String[] args) {

//        setup system and agents
        final ActorSystem system = ActorSystem.create("game");

        final ActorRef environmentAgent = system.actorOf(Props.create(EnvironmentAgent.class), "environment");
        final ActorRef enemyAgent = system.actorOf(Props.create(EnemyAgent.class), "enemy");
        final ActorRef fightAgent = system.actorOf(Props.create(FightAgent.class), "fight");
        final ActorRef playerActionAgent = system.actorOf(
                Props.create(PlayerActionAgent.class, environmentAgent, enemyAgent, fightAgent),"player");

//        final Inbox inbox = Inbox.create(system);
//
////        usage example
//        playerActionAgent.tell(new Turn(Direction.LEFT), ActorRef.noSender());


    }
}
