package pl.edu.agh.game.player.action;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.Timeout;
import pl.edu.agh.game.enemy.EnemyAgent;
import pl.edu.agh.game.environment.EnvironmentAgent;
import pl.edu.agh.game.fight.FightAgent;
import pl.edu.agh.game.model.player.Player;
import pl.edu.agh.game.player.action.messages.*;
import pl.edu.agh.game.player.action.services.action.PlayerAction;
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
        player = new Player(100); // TODO: 5/24/17 enable setting up parameters before starting the game
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

    private void onShowDirections(ShowDirections showDirections) {
        log.info("SHOW DIRECTIONS");
    }

    private void onShowActions(ShowActions showActions) {
        log.info("SHOW ACTIONS");
    }

    private void onMove(Move move) throws Exception {
        log.info("MOVE " + move.getDirection());
    }

    private void onFight(Fight fight) throws Exception {
        log.info("FIGHT with beast");
    }

    private void onActionResponseMessage(ActionResponseMessage actionResponseMessage) {
        getSender().tell(new ActionResponseMessage(), getSelf());
    }


}
