package pl.edu.agh.game.environment.services;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import pl.edu.agh.game.message.environment.CreateMapMessage;
import pl.edu.agh.game.model.map.Action;
import pl.edu.agh.game.model.map.Location;

import java.util.Random;

public class MapCreateService extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateMapMessage.class, s -> {

                    log.info("MapCreateService[" + getSelf() + "] -- Location[][] --> EnvironmentAgent");

                    getSender().tell(initLocations(s.getLocations(), s.getSize()), getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }


    private Location[][] initLocations(Location[][] locations, int size) {

        locations = new Location[size][size];

        for(int i=0; i<locations.length; i++) {
            for(int j=0; j<locations[i].length; j++) {
                locations[i][j] = new Location(getChance(), size*i+j, Action.values()[new Random().nextInt(Action.values().length)]);
            }
        }

        return locations;
    }

    private boolean getChance() {
        return new Random().nextDouble() <= 0.2;
    }


}
