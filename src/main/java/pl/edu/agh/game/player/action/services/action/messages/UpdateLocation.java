package pl.edu.agh.game.player.action.services.action.messages;

import pl.edu.agh.game.player.action.services.action.model.Location;

import java.io.Serializable;

public class UpdateLocation implements Serializable {
    private Location location;

    public UpdateLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
