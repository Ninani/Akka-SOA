package pl.edu.agh.game.player.action.services.action;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import pl.edu.agh.game.model.player.Player;
import pl.edu.agh.game.player.action.services.action.messages.UpdateLifePoints;
import pl.edu.agh.game.player.action.services.action.messages.UpdateLocation;
import pl.edu.agh.game.model.player.Location;

/**
 * UNUSED
*/

public class PlayerAction extends AbstractActor { // TODO: 5/24/17 add persistence

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private Player player;

    @Override
    public void preStart() throws Exception {
        Location location = new Location(10, 10);
        player = new Player("defaultName", location, 100, 0); // TODO: 5/24/17 enable setting up parameters before starting the game
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UpdateLocation.class, this::onUpdateLocation)
                .match(UpdateLifePoints.class, this::onUpdateLifePoints)
                .build();
    }

    private void onUpdateLocation(UpdateLocation updateLocation){
        log.info("before UPDATE LOCATION: " +
                "x="+player.getLocation().getX()+" y="+player.getLocation().getY());
        player.setLocation((Location) updateLocation.getLocation());
        log.info("after UPDATE LOCATION: " +
                "x="+player.getLocation().getX()+" y="+player.getLocation().getY());
    }

    private void onUpdateLifePoints(UpdateLifePoints updateLifePoints){
        log.info("UPDATE LIFE POINTS");
        player.setLifePoints(updateLifePoints.getLifePoints());
    }

}

