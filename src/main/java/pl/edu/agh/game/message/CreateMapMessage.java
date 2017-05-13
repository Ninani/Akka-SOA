package pl.edu.agh.game.message;

import pl.edu.agh.game.model.map.Location;

public class CreateMapMessage {
    private Location[][] locations;
    private int size;

    public CreateMapMessage() {
    }

    public CreateMapMessage(Location[][] locations, int size) {
        this.locations = locations;
        this.size = size;
    }

    public Location[][] getLocations() {
        return locations;
    }

    public void setLocations(Location[][] locations) {
        this.locations = locations;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
