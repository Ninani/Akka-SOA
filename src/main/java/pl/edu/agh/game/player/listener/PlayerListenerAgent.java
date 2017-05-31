package pl.edu.agh.game.player.listener;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import pl.edu.agh.game.player.action.PlayerActionAgent;
import pl.edu.agh.game.player.action.messages.ActionMsg;
import pl.edu.agh.game.player.listener.interpreter.CommandInterpreter;
import pl.edu.agh.game.player.listener.messages.Start;

import java.util.Scanner;

/**
 * CONSOLE COMMANDS INTERPRETER
 * Reads commands written by the user and translates it into messages sent to PlayerAction
 */
public class PlayerListenerAgent extends AbstractActor {    // TODO: 5/18/17 rewrite it with FSM

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

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

    private void onStart(Start start) {
        log.info("Starting console");
        onRun();
    }

    private void onRun() {
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

    private void onClose(){
        log.info("Closing console");
    }
}


