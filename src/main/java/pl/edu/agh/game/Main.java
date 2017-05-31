package pl.edu.agh.game;

import akka.actor.*;
import pl.edu.agh.game.player.listener.PlayerListenerAgent;
import pl.edu.agh.game.player.listener.messages.Start;

public class Main{

    public static void main(String[] args) {

        final ActorSystem system = ActorSystem.create("game-system");
        final ActorRef playerListenerAgent = system.actorOf(Props.create(PlayerListenerAgent.class), "player-listener");
        playerListenerAgent.tell(new Start(), ActorRef.noSender());

    }
}
