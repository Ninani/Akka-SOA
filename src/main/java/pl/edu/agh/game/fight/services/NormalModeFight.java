package pl.edu.agh.game.fight.services;

import pl.edu.agh.game.message.fight.FightMessage;

public class NormalModeFight extends FightService{

    private static final double PLAYER_MULTIPLIER = 1;
    private static final double ENEMY_MULTIPLIER = 1;

    @Override
    protected void handleFightMessage(FightMessage message) {
        resolveFight(
                message.getPlayer(),
                message.getEnemy(),
                PLAYER_MULTIPLIER,
                ENEMY_MULTIPLIER);
    }
}
