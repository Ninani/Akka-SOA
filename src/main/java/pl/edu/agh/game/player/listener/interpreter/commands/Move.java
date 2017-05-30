package pl.edu.agh.game.player.listener.interpreter.commands;

import pl.edu.agh.game.model.map.Direction;
import pl.edu.agh.game.player.action.messages.ActionMsg;
import java.util.HashMap;
import java.util.Map;

/**
 * MOVE [LEFT|RIGHT|UP|DOWN]
 */
public class Move implements Command {

    private static final Map<String, pl.edu.agh.game.player.action.messages.ActionMsg> messages = new HashMap<>();
    static {
        messages.put("down", new pl.edu.agh.game.player.action.messages.Move(Direction.UP));
        messages.put("up", new pl.edu.agh.game.player.action.messages.Move(Direction.UP));
        messages.put("left", new pl.edu.agh.game.player.action.messages.Move(Direction.LEFT));
        messages.put("right", new pl.edu.agh.game.player.action.messages.Move(Direction.RIGHT));
    }

    private static final String NO_ARGS_MSG =
            "NO DIRECTION: please enter direction after move command (MOVE {DOWN/UP/LEFT/RIGHT})";

    @Override
    public ActionMsg execute(String[] args) {
        if (args.length == 0) {
            System.out.println(NO_ARGS_MSG);
        } else {
            if (messages.containsKey(args[0])){
                return messages.get(args[0]);
            }
        }
        return null;
    }
}
