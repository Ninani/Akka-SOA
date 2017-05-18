package pl.edu.agh.game.player.action;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.FI;
import pl.edu.agh.game.enemy.EnemyAgent;
import pl.edu.agh.game.environment.EnvironmentAgent;
import pl.edu.agh.game.fight.FightAgent;
import pl.edu.agh.game.message.environment.MoveMessage;
import pl.edu.agh.game.player.action.messages.ActionResponseMessage;
import pl.edu.agh.game.player.action.messages.Fight;
import pl.edu.agh.game.player.action.messages.Move;
import pl.edu.agh.game.player.action.messages.Turn;
import pl.edu.agh.game.player.action.services.EnemyPlayerAction;
import pl.edu.agh.game.player.action.services.EnvironmentPlayerAction;
import pl.edu.agh.game.player.action.services.FightPlayerAction;

import java.util.ArrayList;
import java.util.List;

/**
 * MASTER PLAYER AGENT
 * Distributes messages to appropriate agents.
 */
public class PlayerActionAgent extends AbstractActor{

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

//    actors for communication
    private ActorRef environmentAgent;
    private ActorRef enemyAgent;
    private ActorRef fightAgent;
//    services
    private ActorRef enemyPlayerAction;
    private ActorRef environmentPlayerAction;
    private ActorRef fightPlayerAction;

    @Override
    public void preStart() throws Exception {
//        actors
        environmentAgent = getContext().actorOf(Props.create(EnvironmentAgent.class), "environment");
        enemyAgent = getContext().actorOf(Props.create(EnemyAgent.class), "enemy");
        fightAgent = getContext().actorOf(Props.create(FightAgent.class), "fight");
//        services
//        enemyPlayerAction = getContext().actorOf(Props.create(EnemyPlayerAction.class, "enemy-player-action"));
//        environmentPlayerAction = getContext().actorOf(Props.create(EnvironmentPlayerAction.class, "environment-player-action"));
//        fightPlayerAction = getContext().actorOf(Props.create(FightPlayerAction.class, "fight-player-action"));

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Turn.class, this::onTurn)
                .match(Move.class, this::onMove)
                .match(Fight.class, this::onFight)
                .match(ActionResponseMessage.class, this::onActionResponseMessage)
                .build();
    }

    private void onTurn(Turn turn) {
        log.info("Turn " + turn.getDirection());
        MoveMessage moveMessage = new MoveMessage();
        moveMessage.setDirection(turn.getDirection());
        environmentAgent.tell(moveMessage, getSelf());
    }

    private void onMove(Move move) {
        log.info("Move " + move.getDirection());
    }

    private void onFight(Fight fight) {
        log.info("Fight with " + fight.getBeast());
    }

    private void onActionResponseMessage(ActionResponseMessage actionResponseMessage) {
        getSender().tell(new ActionResponseMessage("response"), getSelf());
    }


}
