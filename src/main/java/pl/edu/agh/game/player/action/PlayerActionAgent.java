package pl.edu.agh.game.player.action;

import akka.actor.*;
import pl.edu.agh.game.message.environment.MoveMessage;
import pl.edu.agh.game.player.action.messages.ActionResponseMessage;
import pl.edu.agh.game.player.action.messages.Turn;
import pl.edu.agh.game.player.action.services.EnemyPlayerAction;
import pl.edu.agh.game.player.action.services.EnvironmentPlayerAction;
import pl.edu.agh.game.player.action.services.FightPlayerAction;

import java.util.ArrayList;
import java.util.List;


public class PlayerActionAgent extends AbstractActor{

    private final ActorRef environmentAgent;
    private final ActorRef enemyAgent;
    private final ActorRef fightAgent;
    private List<ActorRef> services = new ArrayList<>();

    public PlayerActionAgent(ActorRef environmentAgent, ActorRef enemyAgent, ActorRef fightAgent) {
        this.environmentAgent = environmentAgent;
        this.enemyAgent = enemyAgent;
        this.fightAgent = fightAgent;
    }

    @Override
    public void preStart() throws Exception {
        initServices();
    }

    private void initServices() throws Exception {
        createService(EnemyPlayerAction.class);
        createService(EnvironmentPlayerAction.class);
        createService(FightPlayerAction.class);
    }

    private void createService(Class clazz) throws Exception {
        ActorRef actorRef = getContext().actorOf(Props.create(clazz));
        getContext().watch(actorRef);
        services.add(actorRef);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Turn.class, this::onTurn)
                .match(ActionResponseMessage.class, this::onActionResponseMessage)
                .build();
    }

    private void onTurn(Turn turn) {
        MoveMessage moveMessage = new MoveMessage();
        moveMessage.setDirection(turn.direction);
        environmentAgent.tell(moveMessage, getSelf());
    }

    private void onActionResponseMessage(ActionResponseMessage actionResponseMessage) {
        getSender().tell(new ActionResponseMessage("response"), getSelf());
    }


}
