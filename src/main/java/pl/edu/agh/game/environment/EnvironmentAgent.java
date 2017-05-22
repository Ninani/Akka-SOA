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

import java.util.ArrayList;
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
        mapCreateService = getContext().actorOf(Props.create(MapCreateService.class), "MapCreateService");
        enemyAgent = getContext().actorOf(new RoundRobinPool(3).props(Props.create(EnemyAgent.class)), "enemyAgent");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateMapMessage.class, s -> {

                    log.info("EnvironmentAgent[" + getSelf() + "] -- CreateMapMessage --> MapCreateService");

                    Future<Object> future = Patterns.ask(mapCreateService, new CreateMapMessage(locations, size), timeout);
                    locations = (Location[][]) Await.result(future, timeout.duration());

                    //init all enemies
                    initEnemies();

                    getSender().tell("map created", getSelf());
                })
                .match(ActionMessage.class, s -> {

                    log.info("EnvironmentAgent[" + getSelf() + "] -- Action --> " + getSender());

                    getSender().tell(getAction(s.getPosition()), getSelf());
                })
                .match(DirectionsMessage.class, s -> {

                    log.info("EnvironmentAgent[" + getSelf() + "] -- List<Direction> --> " + getSender());

                    getSender().tell(getDirections(s.getPosition()), getSelf());

                })
                .match(MoveMessage.class, s -> {

                    log.info("EnvironmentAgent[" + getSelf() + "] -- Location> --> " + getSender());

                    Location newLocation = getNewLocation(s.getCurrentPosition() ,s.getDirection());
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

    private void initEnemies() throws Exception {

        for(int i=0; i<locations.length; i++) {
            for(int j=0; j<locations[i].length; j++) {
                if (locations[i][j].getAction().equals(Action.ENEMY)) {

                    if (locations[i][j].getEnemy() == null) {

                        log.info("EnvironmentAgent[" + getSelf() + "] -- NewMonsterMessage --> EnemyAgent");

                        Future<Object> future = Patterns.ask(enemyAgent, new NewMonsterMessage(), timeout);
                        Enemy result = (Enemy) Await.result(future, timeout.duration());

                        locations[i][j].setEnemy(result);
                    }
                }
            }
        }
    }
}

