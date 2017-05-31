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
import pl.edu.agh.game.message.environment.*;
import pl.edu.agh.game.message.fight.DifficultMessage;
import pl.edu.agh.game.message.fight.FightMessage;
import pl.edu.agh.game.model.enemies.Enemy;
import pl.edu.agh.game.model.fight.FightType;
import pl.edu.agh.game.model.map.Action;
import pl.edu.agh.game.model.map.Direction;
import pl.edu.agh.game.model.map.Location;
import pl.edu.agh.game.model.player.Player;
import pl.edu.agh.game.player.action.messages.*;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;


/**
 * MASTER PLAYER AGENT
 * Distributes messages to appropriate agents(services).
 */
public class PlayerActionAgent extends AbstractActor{ // TODO: 5/30/17 send responses to PlayerListenerAgent???

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

        environmentAgent.tell(new CreateMapMessage(), getSelf());
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
        DirectionsMessage directionsMessage = new DirectionsMessage(player.getLocationId());
        Future<Object> directionsFuture = Patterns.ask(environmentAgent, directionsMessage, timeout);
        List<Direction> result = (List<Direction>) Await.result(directionsFuture, timeout.duration());
        log.info("POSSIBLE DIRECTIONS: " + result);
    }

    private void onShowActions(ShowActions showActions) throws Exception {
        log.info("SHOW ACTIONS");
        ActionMessage actionMessage = new ActionMessage(player.getLocationId());
        Future<Object> actionsFuture = Patterns.ask(environmentAgent, actionMessage, timeout);
        Action result = (Action) Await.result(actionsFuture, timeout.duration());
        log.info("POSSIBLE ACTIONS: " + result);
    }

    private void onMove(Move move) throws Exception {
        log.info("MOVE " + move.getDirection());
        log.info("before MOVE: position = "+player.getLocationId());
        MoveMessage moveMessage = new MoveMessage(player.getLocationId(), move.getDirection());
        Future<Object> moveFuture = Patterns.ask(environmentAgent, moveMessage, timeout);
        Location moveResult = (Location) Await.result(moveFuture, timeout.duration());
        player.setLocationId(moveResult.getId());
        log.info("after MOVE: position = " + player.getLocationId());
    }

    private void onFight(Fight fight) throws Exception {
        log.info("FIGHT with beast");
        log.info("before FIGHT: lifePoints="+player.getLifePoints());
        fightAgent.tell(new DifficultMessage(FightType.HARD), getSelf()); // TODO: 5/30/17 difficulty is hardcoded! - set up options at the beginning???
        LocationMessage locationMessage = new LocationMessage(player.getLocationId());
        Future<Object> locationFuture = Patterns.ask(environmentAgent, locationMessage, timeout);
        Location locationResult = (Location) Await.result(locationFuture, timeout.duration());
        Enemy enemy = locationResult.getEnemy();
        log.info("enemy for the current location: " + enemy);

        if (enemy != null) {
            Future<Object> fightFuture = Patterns.ask(fightAgent, new FightMessage(player, enemy), timeout);

            FightMessage fightResultMessage = (FightMessage) Await.result(fightFuture, timeout.duration());
            player.setLifePoints(fightResultMessage.getPlayer().getLifePoints());
            log.info("after FIGHT: lifePoints="+player.getLifePoints());
        } else {
            log.info("no enemy available!");
        }
    }

    private void onActionResponseMessage(ActionResponseMessage actionResponseMessage) {
        getSender().tell(new ActionResponseMessage(), getSelf());
    }


}
