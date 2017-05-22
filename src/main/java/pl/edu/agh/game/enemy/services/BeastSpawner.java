package pl.edu.agh.game.enemy.services;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import pl.edu.agh.game.message.enemy.SpawnUnitMessage;
import pl.edu.agh.game.model.enemies.Enemy;
import pl.edu.agh.game.model.enemies.beasts.Bear;
import pl.edu.agh.game.model.enemies.beasts.QuillBeast;

public class BeastSpawner extends Spawner {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

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
        log.info("BeastSpawner[" + getSelf() + "] -- Beast --> " + getSender());

        getSender().tell(enemy, getSelf());
    }

}

