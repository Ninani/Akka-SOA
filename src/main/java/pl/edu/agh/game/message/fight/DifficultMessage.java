package pl.edu.agh.game.message.fight;

import pl.edu.agh.game.model.fight.FightType;

public class DifficultMessage {
    private final FightType fightType;

    public DifficultMessage(FightType fightType) {
        this.fightType = fightType;
    }

    public FightType getFightType() {
        return fightType;
    }
}
