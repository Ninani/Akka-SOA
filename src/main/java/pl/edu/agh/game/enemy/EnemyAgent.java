package pl.edu.agh.game.enemy;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.routing.ActorRefRoutee;
import akka.routing.RandomPool;
import akka.routing.Routee;
import akka.util.Timeout;
import pl.edu.agh.game.enemy.services.BeastSpawner;
import pl.edu.agh.game.enemy.services.OrcSpawner;
import pl.edu.agh.game.enemy.services.Spawner;
import pl.edu.agh.game.message.NewMonsterMessage;
import pl.edu.agh.game.message.SpawnUnitMessage;
import pl.edu.agh.game.model.Enemy;
import pl.edu.agh.game.model.enemies.EnemyType;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.*;

public class EnemyAgent extends AbstractActor{

    private ActorRef router;
    private Timeout timeout = new Timeout(Duration.create(5, "seconds"));
    private Map<Integer, EnemyType> enemyTypeMap= new HashMap<>();
    private LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public void preStart() throws Exception {
        initRouter();
        initMap();
    }

    private void initRouter(){
        List<Routee> routees = new ArrayList<>();
        addRouteToList(routees, BeastSpawner.class);
        addRouteToList(routees, OrcSpawner.class);
        this.router = getContext().actorOf(new RandomPool(routees.size()).props(Props.create(Spawner.class, routees)));
    }

    private void initMap(){
        enemyTypeMap.put(0, EnemyType.MELEE);
        enemyTypeMap.put(0, EnemyType.RANGED);
    }

    private void addRouteToList(List<Routee> routees, Class clazz){
        ActorRef actorRef = getContext().actorOf(Props.create(clazz));
        getContext().watch(actorRef);
        routees.add(new ActorRefRoutee(actorRef));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(NewMonsterMessage.class, this::createNewMonster)
                .build();
    }

    private void createNewMonster(NewMonsterMessage message){
        Random random = new Random(System.currentTimeMillis());
        EnemyType type = enemyTypeMap.get(random.nextInt(2));
        SpawnUnitMessage spawnUnitMessage = new SpawnUnitMessage(type);
        Future<Object> enemyFuture = Patterns.ask(router, spawnUnitMessage, timeout);
        try {
            Enemy result = (Enemy) Await.result(enemyFuture, timeout.duration());
            getSender().tell(result, getSelf());
        } catch (Exception e) {
            logger.error("Future in EnemyAgent.class : " + e.getMessage());
        }

    }
}
