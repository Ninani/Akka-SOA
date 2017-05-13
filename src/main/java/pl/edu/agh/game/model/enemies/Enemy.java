package pl.edu.agh.game.model.enemies;

public abstract class Enemy {

    private int healthPoints;
    private int damage;

    public Enemy(int healthPoints, int damage) {
        this.healthPoints = healthPoints;
        this.damage = damage;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getDamage() {
        return damage;
    }
}
