package pl.edu.agh.game.player.action.services.action;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import pl.edu.agh.game.model.player.Player;
import pl.edu.agh.game.player.action.services.action.messages.UpdateLifePoints;

/**
 * UNUSED
*/

public class PlayerAction extends AbstractActor { // TODO: 5/24/17 add persistence

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private Player player;

    @Override
    public void preStart() throws Exception {
        player = new Player("defaultName", 10, 100, 0); // TODO: 5/24/17 enable setting up parameters before starting the game
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UpdateLifePoints.class, this::onUpdateLifePoints)
                .build();
    }
    
    private void onUpdateLifePoints(UpdateLifePoints updateLifePoints){
        log.info("UPDATE LIFE POINTS");
        player.setLifePoints(updateLifePoints.getLifePoints());
    }

}

