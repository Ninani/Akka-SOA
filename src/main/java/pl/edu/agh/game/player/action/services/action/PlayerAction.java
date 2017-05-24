package pl.edu.agh.game.player.action.services.action;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import pl.edu.agh.game.player.action.services.action.messages.UpdateLifePoints;
import pl.edu.agh.game.player.action.services.action.messages.UpdateLocation;
import pl.edu.agh.game.player.action.services.action.messages.UpdateStrength;
import pl.edu.agh.game.player.action.services.action.model.Location;
import pl.edu.agh.game.player.action.services.action.model.Player;

public class PlayerAction extends AbstractActor { // TODO: 5/24/17 add persistence

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private Player player;

    @Override
    public void preStart() throws Exception {
        Location location = new Location(10, 10);
        player = new Player("defaultName", location, 100, 100); // TODO: 5/24/17 enable setting up parameters before starting the game
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UpdateLocation.class, this::onUpdateLocation)
                .match(UpdateLifePoints.class, this::onUpdateLifePoints)
                .match(UpdateStrength.class, this::onUpdateStrength)
                .build();
    }

    private void onUpdateLocation(UpdateLocation updateLocation){
        log.info("UPDATE LOCATION");
        player.setLocation((Location) updateLocation.getLocation());
    }

    private void onUpdateLifePoints(UpdateLifePoints updateLifePoints){
        log.info("UPDATE LIFE POINTS");
        player.setLifePoints(updateLifePoints.getLifePoints());
    }

    private void onUpdateStrength(UpdateStrength updateStrength){
        log.info("UPDATE STRENGTH");
        player.setStrength(updateStrength.getStrength());
    }
}

