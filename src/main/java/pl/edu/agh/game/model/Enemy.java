package pl.edu.agh.game.model;

public abstract class Enemy {

    private final int healthPoints;
    private final int damage;

    public Enemy(int healthPoints, int damage) {
        this.healthPoints = healthPoints;
        this.damage = damage;
    }

    public int getHealthPoints() {
        return healthPoints;
    }
    public int getDamage() {
        return damage;
    }
}
