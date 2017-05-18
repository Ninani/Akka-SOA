package pl.edu.agh.game.player.listener;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import pl.edu.agh.game.model.enemies.beasts.Bear;
import pl.edu.agh.game.model.enemies.beasts.Beast;
import pl.edu.agh.game.model.enemies.beasts.QuillBeast;
import pl.edu.agh.game.model.map.Direction;
import pl.edu.agh.game.player.action.PlayerActionAgent;
import pl.edu.agh.game.player.action.messages.Fight;
import pl.edu.agh.game.player.action.messages.Move;
import pl.edu.agh.game.player.listener.messages.Start;
import java.util.Scanner;

/**
 * CONSOLE COMMANDS INTERPRETER
 * Reads commands written by the user and translates it into messages sent to PlayerActionAgent
 */
public class PlayerListenerAgent extends AbstractActor {    // TODO: 5/18/17 rewrite it with FSM

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private ActorRef playerActionAgent;

    @Override
    public void preStart() throws Exception {
        playerActionAgent = getContext().actorOf(Props.create(PlayerActionAgent.class),"player-action");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                . match(Start.class, this::onStart)
                .build();
    }

    public void onStart(Start start) {
        log.info("Starting console");
        onRun();
    }

    public void onRun() {
        log.info("Console is running: please write a command");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if (input.length() == 0){
                System.out.println("Please write a command!");
            } else {
                switch (input) {        // TODO: 5/18/17 create a map with key commands
                    case Commands.QUIT: onClose();
                    break;
                    case Commands.HELP: Commands.printHelp();
                    break;
                    case Commands.MOVE_DOWN: playerActionAgent.tell(new Move(Direction.DOWN), getSender());
                    break;
                    case Commands.MOVE_UP: playerActionAgent.tell(new Move(Direction.UP), getSender());
                    break;
                    case Commands.MOVE_LEFT: playerActionAgent.tell(new Move(Direction.LEFT), getSender());
                    break;
                    case Commands.MOVE_RIGHT: playerActionAgent.tell(new Move(Direction.RIGHT), getSender());
                    break;
                    case Commands.FIGHT_BEAR: playerActionAgent.tell(new Fight(new Bear()), getSender());
                    break;
                    case Commands.FIGHT_QUILL: playerActionAgent.tell(new Fight(new QuillBeast()), getSender());
                    break;
                    default: System.out.println("Wrong command");
                        break;
                }
            }

        }
    }

    public void onClose(){
        log.info("Closing console");
    }


}
