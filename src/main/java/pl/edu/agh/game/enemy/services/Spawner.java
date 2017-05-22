package pl.edu.agh.game.enemy.services;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import pl.edu.agh.game.message.enemy.SpawnUnitMessage;

public abstract class Spawner extends AbstractActor{

    abstract protected void spawnUnit(SpawnUnitMessage message);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SpawnUnitMessage.class, this::spawnUnit)
                .build();
    }
}
