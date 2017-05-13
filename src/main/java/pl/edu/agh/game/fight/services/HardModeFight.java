package pl.edu.agh.game.fight.services;

import pl.edu.agh.game.message.fight.FightMessage;

public class HardModeFight extends FightService{

    private static final double PLAYER_MULTIPLIER = 0.85;
    private static final double ENEMY_MULTIPLIER = 1.25;

    @Override
    protected void handleFightMessage(FightMessage message) {
        resolveFight(
                message.getPlayer(),
                message.getEnemy(),
                PLAYER_MULTIPLIER,
                ENEMY_MULTIPLIER);
    }
}
