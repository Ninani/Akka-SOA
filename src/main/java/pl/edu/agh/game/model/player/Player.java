package pl.edu.agh.game.model.player;

public class Player {

    private String name = "Zdzislaw";
    private int positionOnMap;
    private int lifePoints;
    private int damage;

    public Player(int health, int damage) {
        this.lifePoints = health;
        this.damage = damage;
    }

    public Player(int damage){
        this.name = "defaultName";
        this.positionOnMap = 10;
        this.lifePoints = 100;
        this.damage = damage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getLocation() {
        return positionOnMap;
    }

    public void setLocation(int positionOnMap) {
        this.positionOnMap = positionOnMap;
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

