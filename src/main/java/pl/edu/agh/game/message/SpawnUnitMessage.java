package pl.edu.agh.game.message;

import pl.edu.agh.game.model.enemies.EnemyType;

public class SpawnUnitMessage {

    private final EnemyType enemyType;

    public SpawnUnitMessage(EnemyType enemyType) {
        this.enemyType = enemyType;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }
}
