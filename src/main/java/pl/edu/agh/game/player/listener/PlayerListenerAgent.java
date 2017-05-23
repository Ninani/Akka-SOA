package pl.edu.agh.game.player.listener;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import pl.edu.agh.game.model.enemies.beasts.Bear;
import pl.edu.agh.game.model.enemies.beasts.QuillBeast;
import pl.edu.agh.game.model.map.Direction;
import pl.edu.agh.game.player.action.PlayerActionAgent;
import pl.edu.agh.game.player.action.messages.ActionMsg;
import pl.edu.agh.game.player.action.messages.Fight;
import pl.edu.agh.game.player.action.messages.Move;
import pl.edu.agh.game.player.listener.interpreter.CommandInterpreter;
import pl.edu.agh.game.player.listener.messages.Start;

import java.util.Scanner;

/**
 * CONSOLE COMMANDS INTERPRETER
 * Reads commands written by the user and translates it into messages sent to PlayerActionAgent
 */
public class PlayerListenerAgent extends AbstractActor {    // TODO: 5/18/17 rewrite it with FSM

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private ActorRef playerActionAgent;
    private CommandInterpreter commandInterpreter;

    @Override
    public void preStart() throws Exception {
        playerActionAgent = getContext().actorOf(Props.create(PlayerActionAgent.class),"player-action");
        commandInterpreter = new CommandInterpreter();
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

        boolean processCommand = true;
        while (processCommand) {
            System.out.print("\n");
            String inputLine = scanner.nextLine();
            if (inputLine.length() == 0){
                System.out.println("Please write a command!");
            } else {
                ActionMsg msg = commandInterpreter.executeCommand(inputLine);
                if (msg != null){
                    playerActionAgent.tell(msg, getSender());
                }
            }
        }
        onClose();
    }

    public void onClose(){
        log.info("Closing console");
    }



//                    case CommandInterpreter.FIGHT_BEAR: playerActionAgent.tell(new Fight(new Bear()), getSender());
//                    break;
//                    case CommandInterpreter.FIGHT_QUILL: playerActionAgent.tell(new Fight(new QuillBeast()), getSender());
//                    break;
//                    default: System.out.println("Wrong command");
//                        break;
//                }
}


