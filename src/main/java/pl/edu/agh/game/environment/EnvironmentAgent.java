package pl.edu.agh.game.environment;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import pl.edu.agh.game.*;
import pl.edu.agh.game.model.Enemy;
import pl.edu.agh.game.model.map.Action;
import pl.edu.agh.game.enemy.EnemyAgent;
import pl.edu.agh.game.message.ActionMessage;
import pl.edu.agh.game.message.DirectionsMessage;
import pl.edu.agh.game.message.MoveMessage;
import pl.edu.agh.game.environment.services.MapCreateService;
import pl.edu.agh.game.message.NewMonsterMessage;
import pl.edu.agh.game.model.map.Direction;
import pl.edu.agh.game.model.map.Location;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;


public class EnvironmentAgent extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private Props props = Props.create(MapCreateService.class, () -> new MapCreateService());
    private ActorRef mapService = getContext().actorOf(props);

    private Props enemyProps = Props.create(EnemyAgent.class, () -> new EnemyAgent());
    private ActorRef enemyAgent = getContext().actorOf(enemyProps);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ActionMessage.class, s -> {

                    //ask mapService
                    Timeout timeout = new Timeout(Duration.create(5, "seconds"));
                    Future<Object> future = Patterns.ask(mapService, new ActionMessage(), timeout);
                    Action result = (Action) Await.result(future, timeout.duration());

                    //tell the sender the result
                    getSender().tell(result, getSelf());
                })
                .match(DirectionsMessage.class, s -> {

                    //ask mapService
                    Timeout timeout = new Timeout(Duration.create(5, "seconds"));
                    Future<Object> future = Patterns.ask(mapService, new DirectionsMessage(), timeout);
                    List<Direction> result = (List<Direction>) Await.result(future, timeout.duration());

                    //tell the sender the result
                    getSender().tell(result, getSelf());
                })
                .match(MoveMessage.class, s -> {

                    //ask mapservice to move to another location
                    Timeout timeout = new Timeout(Duration.create(5, "seconds"));
                    Future<Object> future = Patterns.ask(mapService, new MoveMessage(), timeout);
                    Location result = (Location) Await.result(future, timeout.duration());

                    //return current Location with proper objects
                    getSender().tell(result, getSelf());

                })
                .match(NewMonsterMessage.class, s -> {

                    //ask mapservice to move to another location
                    Timeout timeout = new Timeout(Duration.create(5, "seconds"));
                    Future<Object> future = Patterns.ask(enemyAgent, new NewMonsterMessage(), timeout);
                    Enemy result = (Enemy) Await.result(future, timeout.duration());

                    //return current Location with proper objects
                    getSender().tell(result, getSelf());

                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}

