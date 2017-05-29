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
import pl.edu.agh.game.message.environment.MoveMessage;
import pl.edu.agh.game.message.fight.DifficultMessage;
import pl.edu.agh.game.message.fight.FightMessage;
import pl.edu.agh.game.model.enemies.Enemy;
import pl.edu.agh.game.model.fight.FightType;
import pl.edu.agh.game.model.map.Action;
import pl.edu.agh.game.model.map.Location;
import pl.edu.agh.game.model.player.Player;
import pl.edu.agh.game.player.action.messages.*;
import pl.edu.agh.game.player.action.services.action.PlayerAction;
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
//        services
        playerAction = getContext().actorOf(Props.create(PlayerAction.class), "player_action");
//        initialize model
        player = new Player("defaultName", 10, 100, 100); // TODO: 5/24/17 enable setting up parameters before starting the game
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Turn.class, this::onTurn)
                .match(Move.class, this::onMove)
                .match(Fight.class, this::onFight)
                .match(ShowActions.class, this::onShowActions)
                .match(ActionResponseMessage.class, this::onActionResponseMessage)
                .build();
    }

    private void onShowActions(ShowActions showActions) {
        log.info("SHOW ACTIONS");

    }

    private void onTurn(Turn turn) {
        log.info("Turn " + turn.getDirection());
        MoveMessage moveMessage = new MoveMessage();
        moveMessage.setDirection(turn.getDirection());
        environmentAgent.tell(moveMessage, getSelf());
    }

    private void onMove(Move move) throws Exception {
        log.info("MOVE " + move.getDirection());
//        log.info("before MOVE: " +
//                "x="+player.getLocation().getX()+" y="+player.getLocation().getY());
//        MoveMessage moveMessage = new MoveMessage(player.getLocation().getX(), move.getDirection());
//        Future<Object> moveFuture = Patterns.ask(environmentAgent, moveMessage, timeout);
//        MoveMessage moveResult = (MoveMessage) Await.result(moveFuture, timeout.duration());
//        int x = moveResult.getCurrentPosition();
//        int y = moveResult.getCurrentPosition();
////        UpdateLocation updateLocation = new UpdateLocation(new Location(x, y));
////        playerAction.tell(updateLocation, getSelf());
//        player.setLocation(new Location(x, y));
//        log.info("after UPDATE LOCATION: " +
//                "x="+player.getLocation().getX()+" y="+player.getLocation().getY());
    }

    private void onFight(Fight fight) throws Exception {
        log.info("FIGHT with " + fight.getBeast());
        log.info("before FIGHT: lifePoints="+player.getLifePoints());
        fightAgent.tell(new DifficultMessage(FightType.HARD), getSelf());
        Future<Object> enemyFuture = Patterns.ask(enemyAgent, new NewMonsterMessage(), timeout);
        Enemy result = (Enemy) Await.result(enemyFuture, timeout.duration());
        System.out.println(result.getHealthPoints() + " " + result.getDamage());
        Future<Object> fightFuture = Patterns.ask(fightAgent, new FightMessage(player, result), timeout);
        FightMessage fightResultMesaage = (FightMessage) Await.result(fightFuture, timeout.duration());
//        UpdateLifePoints updateLifePoints = new UpdateLifePoints(50);
//        playerAction.tell(updateLifePoints, getSelf());
        player.setLifePoints(fightResultMesaage.getPlayer().getLifePoints());
        log.info("after FIGHT: lifePoints="+player.getLifePoints());
    }

    private void onActionResponseMessage(ActionResponseMessage actionResponseMessage) {
        getSender().tell(new ActionResponseMessage("response"), getSelf());
    }


}
