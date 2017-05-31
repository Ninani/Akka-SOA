package pl.edu.agh.game.model.player;

public class Player {

    private String name;
    private int locationId;
    private int lifePoints;
    private int damage;

    public Player(int health, int damage) {
        this.lifePoints = health;
        this.damage = damage;
    }

    public Player(int damage){
        this.name = "defaultName";
        this.locationId= 10;
        this.lifePoints = 100;
        this.damage = damage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
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

