package pl.edu.agh.game.message.fight;

import pl.edu.agh.game.model.enemies.Enemy;
import pl.edu.agh.game.model.player.Player;

public class FightMessage {
    private final Player player;
    private final Enemy enemy;

    public FightMessage(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy getEnemy() {
        return enemy;
    }
}
