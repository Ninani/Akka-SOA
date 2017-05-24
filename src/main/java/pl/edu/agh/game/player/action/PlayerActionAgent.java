package pl.edu.agh.game.player.action;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import pl.edu.agh.game.enemy.EnemyAgent;
import pl.edu.agh.game.environment.EnvironmentAgent;
import pl.edu.agh.game.fight.FightAgent;
import pl.edu.agh.game.message.environment.MoveMessage;
import pl.edu.agh.game.player.action.messages.ActionResponseMessage;
import pl.edu.agh.game.player.action.messages.Fight;
import pl.edu.agh.game.player.action.messages.Move;
import pl.edu.agh.game.player.action.messages.Turn;
import pl.edu.agh.game.player.action.services.action.PlayerAction;
import pl.edu.agh.game.player.action.services.action.messages.UpdateLifePoints;
import pl.edu.agh.game.player.action.services.action.messages.UpdateLocation;
import pl.edu.agh.game.player.action.services.action.model.Location;

/**
 * MASTER PLAYER AGENT
 * Distributes messages to appropriate agents(services).
 */
public class PlayerActionAgent extends AbstractActor{

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

//    actors for communication
    private ActorRef environmentAgent;
    private ActorRef enemyAgent;
    private ActorRef fightAgent;
//    services
    private ActorRef playerActionAgent;

    @Override
    public void preStart() throws Exception {
//        actors
        environmentAgent = getContext().actorOf(Props.create(EnvironmentAgent.class), "environment");
        enemyAgent = getContext().actorOf(Props.create(EnemyAgent.class), "enemy");
        fightAgent = getContext().actorOf(Props.create(FightAgent.class), "fight");
//        services
        playerActionAgent = getContext().actorOf(Props.create(PlayerAction.class), "player_action");
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
        // TODO: 5/24/17  get location from environment agent and put into message
        UpdateLocation updateLocation = new UpdateLocation(new Location(10, 20));
        playerActionAgent.tell(updateLocation, getSelf());
    }

    private void onFight(Fight fight) {
        log.info("Fight with " + fight.getBeast());
        // TODO: 5/24/17  get life points from fight agent and put into message
        UpdateLifePoints updateLifePoints = new UpdateLifePoints(50);
        playerActionAgent.tell(updateLifePoints, getSelf());
    }

    private void onActionResponseMessage(ActionResponseMessage actionResponseMessage) {
        getSender().tell(new ActionResponseMessage("response"), getSelf());
    }


}
