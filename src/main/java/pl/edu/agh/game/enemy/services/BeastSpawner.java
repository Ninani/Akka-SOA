package pl.edu.agh.game.enemy.services;

import pl.edu.agh.game.message.SpawnUnitMessage;
import pl.edu.agh.game.model.Enemy;
import pl.edu.agh.game.model.enemies.beasts.Bear;
import pl.edu.agh.game.model.enemies.beasts.QuillBeast;

public class BeastSpawner extends Spawner {

    protected void spawnUnit(SpawnUnitMessage message){
        Enemy enemy = null;
        switch (message.getEnemyType()){
            case MELEE:
                enemy = new Bear();
                break;
            case RANGED:
                enemy = new QuillBeast();
                break;
        }
        getSender().tell(enemy, getSelf());
    }

}

