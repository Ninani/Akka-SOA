package pl.edu.agh.game.environment;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.routing.*;
import akka.util.Timeout;
import pl.edu.agh.game.enemy.EnemyAgent;
import pl.edu.agh.game.environment.services.MapCreateService;
import pl.edu.agh.game.message.environment.*;
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
    private static final int SIZE = 10;

    @Override
    public void preStart() throws Exception {
        mapCreateService = getContext().actorOf(Props.create(MapCreateService.class), "MapCreateService");
        enemyAgent = getContext().actorOf(new RoundRobinPool(3).props(Props.create(EnemyAgent.class)), "enemyAgent");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateMapMessage.class, s -> {

                    log.info("EnvironmentAgent -- CreateMapMessage --> MapCreateService");

                    Future<Object> future = Patterns.ask(mapCreateService, new CreateMapMessage(locations, SIZE), timeout);
                    locations = (Location[][]) Await.result(future, timeout.duration());

                    //init all enemies
                    initEnemies();

                    getSender().tell("map created", getSelf());
                })
                .match(ActionMessage.class, s -> {

                    log.info("EnvironmentAgent -- Action --> " + getSender());

                    getSender().tell(getAction(s.getPosition()), getSelf());
                })
                .match(DirectionsMessage.class, s -> {

                    log.info("EnvironmentAgent -- List<Direction> --> " + getSender());

                    getSender().tell(getDirections(s.getPosition()), getSelf());

                })
                .match(MoveMessage.class, s -> {

                    log.info("EnvironmentAgent -- Location> --> " + getSender());

                    Location newLocation = getNewLocation(s.getCurrentPosition() ,s.getDirection());
                    getSender().tell(newLocation, getSelf());
                })
                .match(LocationMessage.class, s -> {
                    log.info("EnvironmentAgent[" + getSelf() + "] -- Location> --> " + getSender());
                    int id = s.getCurrentPosition();
                    Location currentLocation = locations[id/SIZE][id%SIZE];
                    getSender().tell(currentLocation, getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    private Action getAction(int location_id) {
        return locations[location_id/ SIZE][location_id% SIZE].getAction();
    }

    private List<Direction> getDirections(int location_id) {

        int i = location_id/ SIZE;
        int j = location_id% SIZE;

        List<Direction> directions = new LinkedList<>();

        if (i>0 && !locations[i-1][j].isBlank()) {
            directions.add(Direction.UP);
        }
        if (i< SIZE -1 && !locations[i+1][j].isBlank()) {
            directions.add(Direction.DOWN);
        }
        if (j>0 && !locations[i][j-1].isBlank()) {
            directions.add(Direction.LEFT);
        }
        if (j< SIZE -1 && !locations[i][j+1].isBlank()) {
            directions.add(Direction.RIGHT);
        }

        return directions;
    }

    private Location getNewLocation(int currentPosition, Direction direction) {

        Location location = null;
        int i = currentPosition/ SIZE;
        int j = currentPosition% SIZE;

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

    private void initEnemies() throws Exception {

        for (Location[] location : locations) {
            for (Location aLocation : location) {
                if (aLocation.getAction().equals(Action.ENEMY)) {

                    if (aLocation.getEnemy() == null) {

                        log.info("EnvironmentAgent -- NewMonsterMessage --> EnemyAgent");

                        Future<Object> future = Patterns.ask(enemyAgent, new NewMonsterMessage(), timeout);
                        Enemy result = (Enemy) Await.result(future, timeout.duration());

                        aLocation.setEnemy(result);
                    }
                }
            }
        }
    }
}

