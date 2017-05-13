package pl.edu.agh.game.model.player;

public class Player {

    private int healthPoints;
    private int damage;

    public Player(int health, int damage) {
        this.healthPoints = health;
        this.damage = damage;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
