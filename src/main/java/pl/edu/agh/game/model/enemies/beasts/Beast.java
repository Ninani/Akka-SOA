package pl.edu.agh.game.model.enemies.beasts;

import pl.edu.agh.game.model.enemies.Enemy;

public abstract class Beast extends Enemy{
    public Beast(int healthPoints, int damage) {
        super(healthPoints, damage);
    }
}
