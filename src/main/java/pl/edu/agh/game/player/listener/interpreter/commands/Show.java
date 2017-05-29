package pl.edu.agh.game.player.listener.interpreter.commands;

import org.omg.CORBA.PRIVATE_MEMBER;
import pl.edu.agh.game.message.environment.ActionMessage;
import pl.edu.agh.game.player.action.messages.ActionMsg;
import pl.edu.agh.game.player.action.messages.ShowActions;

import java.util.HashMap;
import java.util.Map;

/**
 * SHOW [ACTIONS]
 */
public class Show implements Command{

    private static final Map<String, ActionMsg> messages = new HashMap<>();
    static {
        messages.put("actions", new ShowActions());
    }

    private static final String NO_ARGS_MSG =
            "WRONG COMMAND FORMAT: please enter the show command in the following format: SHOW {ACTIONS}";

    private static final String WRONG_ARG =
            "WRONG ARGUMENT: there is no action for the given argument, please type: SHOW {ACTIONS}";



    @Override
    public ActionMsg execute(String[] args) {
        if (args.length == 0) {
            System.out.println(NO_ARGS_MSG);
        } else {
            if (messages.containsKey(args[0])) {
                return messages.get(args[0]);
            } else {
                System.out.println(WRONG_ARG);
            }
        }
        return null;
    }
}

//    @Override
//    public ActionMsg execute(String[] args) {
//        if (args.length == 0) {
//            System.out.println(NO_ARGS_MSG);
//        } else {
//            if (directions.containsKey(args[0])){
//                return messages.get(directions.get(args[0]));
//            }
//        }
//        return null;
//    }

