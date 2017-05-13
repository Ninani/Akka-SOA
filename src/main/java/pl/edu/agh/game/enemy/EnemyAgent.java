package pl.edu.agh.game.enemy;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import pl.edu.agh.game.enemy.services.BeastSpawner;
import pl.edu.agh.game.enemy.services.OrcSpawner;
import pl.edu.agh.game.message.NewMonsterMessage;
import pl.edu.agh.game.message.SpawnUnitMessage;
import pl.edu.agh.game.model.Enemy;
import pl.edu.agh.game.model.enemies.EnemyType;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.*;

public class EnemyAgent extends AbstractActor{

    private List<ActorRef> spawners = new ArrayList<>();
    private Timeout timeout = new Timeout(Duration.create(5, "seconds"));
    private Map<Integer, EnemyType> enemyTypeMap= new HashMap<>();
    private LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);
    private Random random = new Random();

    @Override
    public void preStart() throws Exception {
        initServices();
        initMap();
    }

    private void initServices(){
        addRouteToList(spawners, BeastSpawner.class);
        addRouteToList(spawners, OrcSpawner.class);
    }

    private void addRouteToList(List<ActorRef> spawners, Class clazz){
        ActorRef actorRef = getContext().actorOf(Props.create(clazz));
        getContext().watch(actorRef);
        spawners.add(actorRef);
    }

    private void initMap(){
        enemyTypeMap.put(0, EnemyType.MELEE);
        enemyTypeMap.put(1, EnemyType.RANGED);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(NewMonsterMessage.class, this::createNewMonster)
                .build();
    }

    private void createNewMonster(NewMonsterMessage message){
        SpawnUnitMessage spawnUnitMessage = new SpawnUnitMessage(getRandomEnemyType());
        Future<Object> enemyFuture = Patterns.ask(getRandomService(), spawnUnitMessage, timeout);
        try {
            Enemy result = (Enemy) Await.result(enemyFuture, timeout.duration());
            getSender().tell(result, getSelf());
        } catch (Exception e) {
            logger.error("Future in EnemyAgent.class : " + e.getMessage());
        }
    }

    private EnemyType getRandomEnemyType(){
        return enemyTypeMap.get(random.nextInt(2));
    }

    private ActorRef getRandomService(){
        return spawners.get(random.nextInt(spawners.size()));
    }
}
