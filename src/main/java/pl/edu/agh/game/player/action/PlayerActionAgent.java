package pl.edu.agh.game.player.action;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import pl.edu.agh.game.enemy.EnemyAgent;
import pl.edu.agh.game.environment.EnvironmentAgent;
import pl.edu.agh.game.fight.FightAgent;
import pl.edu.agh.game.message.enemy.NewMonsterMessage;
import pl.edu.agh.game.message.environment.ActionMessage;
import pl.edu.agh.game.message.environment.DirectionsMessage;
import pl.edu.agh.game.message.environment.MoveMessage;
import pl.edu.agh.game.message.fight.DifficultMessage;
import pl.edu.agh.game.message.fight.FightMessage;
import pl.edu.agh.game.model.enemies.Enemy;
import pl.edu.agh.game.model.fight.FightType;
import pl.edu.agh.game.model.player.Player;
import pl.edu.agh.game.player.action.messages.*;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;


/**
 * MASTER PLAYER AGENT
 * Distributes messages to appropriate agents(services).
 */
public class PlayerActionAgent extends AbstractActor{

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private Timeout timeout = new Timeout(Duration.create(1, "seconds"));

//    actors for communication
    private ActorRef environmentAgent;
    private ActorRef enemyAgent;
    private ActorRef fightAgent;
//    services
    private ActorRef playerAction;
//    player model
    private Player player;

    @Override
    public void preStart() throws Exception {
//        actors
        environmentAgent = getContext().actorOf(Props.create(EnvironmentAgent.class), "environment");
        enemyAgent = getContext().actorOf(Props.create(EnemyAgent.class), "enemy");
        fightAgent = getContext().actorOf(Props.create(FightAgent.class), "fight");
//        initialize model
        player = new Player(100);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ShowDirections.class, this::onShowDirections)
                .match(ShowActions.class, this::onShowActions)
                .match(Move.class, this::onMove)
                .match(Fight.class, this::onFight)
                .match(ActionResponseMessage.class, this::onActionResponseMessage)
                .build();
    }

    private void onShowDirections(ShowDirections showDirections) throws Exception {
        log.info("SHOW DIRECTIONS");
        DirectionsMessage directionsMessage = new DirectionsMessage(player.getLocation());
        Future<Object> directionsFuture = Patterns.ask(environmentAgent, directionsMessage, timeout);
        DirectionsMessage result = (DirectionsMessage) Await.result(directionsFuture, timeout.duration());
        // TODO: 5/30/17 how get possible directions?
        // TODO: 5/30/17 investigate why EnvironmentAgent is blocked
        log.info("POSSIBLE DIRECTIONS: ");
    }

    private void onShowActions(ShowActions showActions) throws Exception {
        log.info("SHOW ACTIONS");
        ActionMessage actionMessage = new ActionMessage(player.getLocation());
        Future<Object> actionsFuture = Patterns.ask(environmentAgent, actionMessage, timeout);
        ActionMessage result = (ActionMessage) Await.result(actionsFuture, timeout.duration());
        // TODO: 5/30/17 how get possible actions?
        log.info("POSSIBLE ACTIONS: ");
    }

    private void onMove(Move move) throws Exception {
        log.info("MOVE " + move.getDirection());
        log.info("before MOVE: position = "+player.getLocation());
        MoveMessage moveMessage = new MoveMessage(player.getLocation(), move.getDirection());
        Future<Object> moveFuture = Patterns.ask(environmentAgent, moveMessage, timeout);
        MoveMessage moveResult = (MoveMessage) Await.result(moveFuture, timeout.duration());
        // TODO: 5/30/17 environment agent is blocked!
        player.setLocation(moveResult.getCurrentPosition());
        log.info("after MOVE: position = "+player.getLocation());
    }

    private void onFight(Fight fight) throws Exception {
        log.info("FIGHT with beast");
        log.info("before FIGHT: lifePoints="+player.getLifePoints());
        fightAgent.tell(new DifficultMessage(FightType.HARD), getSelf()); // TODO: 5/30/17 difficulty is hardcoded!
        Future<Object> enemyFuture = Patterns.ask(enemyAgent, new NewMonsterMessage(), timeout);
        Enemy result = (Enemy) Await.result(enemyFuture, timeout.duration());
        System.out.println(result.getHealthPoints() + " " + result.getDamage());
        Future<Object> fightFuture = Patterns.ask(fightAgent, new FightMessage(player, result), timeout);
        FightMessage fightResultMessage = (FightMessage) Await.result(fightFuture, timeout.duration());
        player.setLifePoints(fightResultMessage.getPlayer().getLifePoints());
        log.info("after FIGHT: lifePoints="+player.getLifePoints());
    }

    private void onActionResponseMessage(ActionResponseMessage actionResponseMessage) {
        getSender().tell(new ActionResponseMessage(), getSelf());
    }


}
