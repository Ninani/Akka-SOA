package pl.edu.agh.game.environment.services;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import pl.edu.agh.game.model.Enemy;
import pl.edu.agh.game.model.map.Direction;
import pl.edu.agh.game.model.map.Action;
import pl.edu.agh.game.model.map.Location;
import pl.edu.agh.game.message.ActionMessage;
import pl.edu.agh.game.message.DirectionsMessage;
import pl.edu.agh.game.message.MoveMessage;
import pl.edu.agh.game.message.NewMonsterMessage;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MapCreateService extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private Location[][] locations = new Location[10][10];

    public MapCreateService() {
        initLocations();
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ActionMessage.class, s -> {
                    getSender().tell(getAction(s.getPosition()), getSelf());
                })
                .match(DirectionsMessage.class, s -> {
                    getSender().tell(getDirections(s.getPosition()), getSelf());
                })
                .match(MoveMessage.class, s -> {

                    Location newLocation = getNewLocation(s.getCurrentPosition() ,s.getDirection());
                    //init all fields
                    if (newLocation.getAction().equals(Action.ENEMY)) {
                        if (newLocation.getEnemy() == null) {
                            //ask for new monster
                            Timeout timeout = new Timeout(Duration.create(5, "seconds"));
                            Future<Object> future = Patterns.ask(getSender(), new NewMonsterMessage(), timeout);
                            Enemy result = (Enemy) Await.result(future, timeout.duration());

                            newLocation.setEnemy(result);
                        }
                    }

                    //return new initialized field
                    getSender().tell(newLocation, getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }


    private void initLocations() {
        for(int i=0; i<locations.length; i++) {
            for(int j=0; j<locations[i].length; j++) {
                locations[i][j] = new Location(getChance(), i+j, Action.values()[new Random().nextInt(Action.values().length)]);
            }
        }
    }

    private boolean getChance() {
        return new Random().nextDouble() <= 0.2;
    }

    //returns randomly chosen action
    private Action getAction(int location_id) {
        return locations[location_id/10][location_id%10].getAction();
    }

    //returns all possible move directions
    private List<Direction> getDirections(int location_id) {

        int i = location_id/10;
        int j = location_id%10;

        Location location = locations[i][j];
        List<Direction> directions = new LinkedList<>();

        if (locations[i-1][j] != null && !locations[i-1][j].isBlank()) {
            directions.add(Direction.LEFT);
        }
        if (locations[i+1][j] != null && !locations[i+1][j].isBlank()) {
            directions.add(Direction.RIGHT);
        }
        if (locations[i][j-1] != null && !locations[i][j-1].isBlank()) {
            directions.add(Direction.UP);
        }
        if (locations[i][j+1] != null && !locations[i][j+1].isBlank()) {
            directions.add(Direction.DOWN);
        }

        return directions;
    }

    private Location getNewLocation(int currentPosition, Direction direction) {

        Location location = null;
        int i = currentPosition/10;
        int j = currentPosition%10;

        if (direction.equals(Direction.LEFT)) {
            location = locations[i-1][j];
        } else if (direction.equals(Direction.RIGHT)) {
            location = locations[i+1][j];
        } else if (direction.equals(Direction.UP)) {
            location = locations[i][j+1];
        } else if (direction.equals(Direction.DOWN)) {
            location = locations[i][j+1];
        }

        return location;
    }
}
