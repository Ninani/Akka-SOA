package pl.edu.agh.game.fight;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import pl.edu.agh.game.fight.services.EasyModeFight;
import pl.edu.agh.game.fight.services.HardModeFight;
import pl.edu.agh.game.fight.services.NormalModeFight;
import pl.edu.agh.game.message.fight.DifficultMessage;
import pl.edu.agh.game.message.fight.FightMessage;
import pl.edu.agh.game.model.fight.FightType;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.Map;

public class FightAgent extends AbstractActor{

    private ActorRef fightService;
    private Map<FightType, Class> serviceTypeMap = new HashMap<>();
    private LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);
    private Timeout timeout = new Timeout(Duration.create(5, "seconds"));

    @Override
    public void preStart() throws Exception {
        serviceTypeMap.put(FightType.EASY, EasyModeFight.class);
        serviceTypeMap.put(FightType.NORMAL, NormalModeFight.class);
        serviceTypeMap.put(FightType.HARD, HardModeFight.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(FightMessage.class, this::handleFightMessage)
                .match(DifficultMessage.class, this::handleModeMessage)
                .build();
    }

    private void handleFightMessage(FightMessage message) {
        Future<Object> enemyFuture = Patterns.ask(fightService, message, timeout);
        try {
            FightMessage result = (FightMessage) Await.result(enemyFuture, timeout.duration());

            logger.info("FightAgent -- FightMessage --> " + getSender());

            getSender().tell(result, getSelf());
        } catch (Exception e) {
            logger.error("Future in FightAgent.class : " + e.getMessage());
        }
    }

    private void handleModeMessage(DifficultMessage message) {
        fightService = getContext().actorOf(Props.create(serviceTypeMap.get(message.getFightType())));
    }
}
