package pl.edu.agh.game.model.player;

public class Player {

    private String name;
    private Location location;
    private int lifePoints;
    private int damage;

    public Player(int health, int damage) {
        this.lifePoints = health;
        this.damage = damage;
    }

    public Player(String name, Location location, int lifePoints, int damage){
        this.name = name;
        this.location = location;
        this.lifePoints = lifePoints;
        this.damage = damage;
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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}

