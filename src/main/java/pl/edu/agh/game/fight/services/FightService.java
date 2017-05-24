package pl.edu.agh.game.fight.services;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import pl.edu.agh.game.message.fight.FightMessage;
import pl.edu.agh.game.model.enemies.Enemy;
import pl.edu.agh.game.model.player.Player;

public abstract class FightService extends AbstractActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    protected abstract void handleFightMessage(FightMessage message);

    protected void resolveFight(Player player, Enemy enemy, double playerDamageChange, double enemyDamageChange){
        while(enemy.getHealthPoints() > 0 && player.getLifePoints() > 0){
            player.setLifePoints(player.getLifePoints() - (int)(enemy.getDamage() * enemyDamageChange));
            enemy.setHealthPoints(enemy.getHealthPoints() - (int)(player.getDamage() * playerDamageChange));
        }
        log.info("FightService[" + getSelf() + "] -- FightMessage --> " + getSender());

        getSender().tell(new FightMessage(player, enemy), getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(FightMessage.class, this::handleFightMessage)
                .build();
    }
}
