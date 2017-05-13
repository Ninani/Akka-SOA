package pl.edu.agh.game.enemy.services;

import pl.edu.agh.game.message.enemy.SpawnUnitMessage;
import pl.edu.agh.game.model.enemies.Enemy;
import pl.edu.agh.game.model.enemies.orcs.Archer;
import pl.edu.agh.game.model.enemies.orcs.Grunt;

public class OrcSpawner extends Spawner {

    protected void spawnUnit(SpawnUnitMessage message) {
        Enemy enemy = null;
        switch (message.getEnemyType()) {
            case MELEE:
                enemy = new Grunt();
                break;
            case RANGED:
                enemy = new Archer();
                break;
        }
        getSender().tell(enemy, getSelf());
    }

}
