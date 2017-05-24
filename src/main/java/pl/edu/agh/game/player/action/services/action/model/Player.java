package pl.edu.agh.game.player.action.services.action.model;

public class Player {

    private String name;
    private Location location;
    private int lifePoints = 0;
    private int strength;

    public Player(String name, Location location, int lifePoints, int strength){
        this.name = name;
        this.location = location;
        this.lifePoints = lifePoints;
        this.strength = strength;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

}
