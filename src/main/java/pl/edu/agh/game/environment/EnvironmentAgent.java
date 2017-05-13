package pl.edu.agh.game.environment;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import pl.edu.agh.game.enemy.EnemyAgent;
import pl.edu.agh.game.environment.services.MapCreateService;
import pl.edu.agh.game.message.environment.ActionMessage;
import pl.edu.agh.game.message.environment.CreateMapMessage;
import pl.edu.agh.game.message.environment.DirectionsMessage;
import pl.edu.agh.game.message.environment.MoveMessage;
import pl.edu.agh.game.message.enemy.NewMonsterMessage;
import pl.edu.agh.game.model.enemies.Enemy;
import pl.edu.agh.game.model.map.Action;
import pl.edu.agh.game.model.map.Direction;
import pl.edu.agh.game.model.map.Location;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.LinkedList;
import java.util.List;


public class EnvironmentAgent extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private Timeout timeout = new Timeout(Duration.create(5, "seconds"));

    private ActorRef enemyAgent;
    private ActorRef mapCreateService;

    private Location[][] locations;
    private int size = 10;

    @Override
    public void preStart() throws Exception {
        enemyAgent = getContext().actorOf(Props.create(EnemyAgent.class));
        mapCreateService = getContext().actorOf(Props.create(MapCreateService.class));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateMapMessage.class, s -> {

                    Future<Object> future = Patterns.ask(mapCreateService, new CreateMapMessage(locations, size), timeout);
                    locations = (Location[][]) Await.result(future, timeout.duration());

                    getSender().tell("map created", getSelf());
                })
                .match(ActionMessage.class, s -> {

                    getSender().tell(getAction(s.getPosition()), getSelf());
                })
                .match(DirectionsMessage.class, s -> {

                    getSender().tell(getDirections(s.getPosition()), getSelf());
                })
                .match(MoveMessage.class, s -> {

                    Location newLocation = getNewLocation(s.getCurrentPosition() ,s.getDirection());

                    if (newLocation.getAction().equals(Action.ENEMY)) {

                        if (newLocation.getEnemy() == null) {

                            Timeout timeout = new Timeout(Duration.create(5, "seconds"));
                            Future<Object> future = Patterns.ask(enemyAgent, new NewMonsterMessage(), timeout);
                            Enemy result = (Enemy) Await.result(future, timeout.duration());

                            newLocation.setEnemy(result);
                        }
                    }

                    getSender().tell(newLocation, getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    private Action getAction(int location_id) {
        return locations[location_id/size][location_id%size].getAction();
    }

    private List<Direction> getDirections(int location_id) {

        int i = location_id/size;
        int j = location_id%size;

        List<Direction> directions = new LinkedList<>();

        if (i>0 && !locations[i-1][j].isBlank()) {
            directions.add(Direction.UP);
        }
        if (i<size-1 && !locations[i+1][j].isBlank()) {
            directions.add(Direction.DOWN);
        }
        if (j>0 && !locations[i][j-1].isBlank()) {
            directions.add(Direction.LEFT);
        }
        if (j<size-1 && !locations[i][j+1].isBlank()) {
            directions.add(Direction.RIGHT);
        }

        return directions;
    }

    private Location getNewLocation(int currentPosition, Direction direction) {

        Location location = null;
        int i = currentPosition/size;
        int j = currentPosition%size;

        if (direction.equals(Direction.UP)) {
            location = locations[i-1][j];
        } else if (direction.equals(Direction.DOWN)) {
            location = locations[i+1][j];
        } else if (direction.equals(Direction.LEFT)) {
            location = locations[i][j-1];
        } else if (direction.equals(Direction.RIGHT)) {
            location = locations[i][j+1];
        }

        return location;
    }
}

