package pl.edu.agh.game.player.listener.interpreter.commands;

import pl.edu.agh.game.model.map.Direction;
import pl.edu.agh.game.player.action.messages.ActionMsg;
import java.util.HashMap;
import java.util.Map;

public class Move implements Command {

    private static final Map<String, Direction> directions = new HashMap<>();
    static {
        directions.put("down", Direction.DOWN);
        directions.put("up", Direction.UP);
        directions.put("left", Direction.LEFT);
        directions.put("right", Direction.RIGHT);
    }

    private static final Map<Direction, pl.edu.agh.game.player.action.messages.ActionMsg> messages = new HashMap<>();
    static {
        messages.put(Direction.DOWN, new pl.edu.agh.game.player.action.messages.Move(Direction.UP));
        messages.put(Direction.UP, new pl.edu.agh.game.player.action.messages.Move(Direction.UP));
        messages.put(Direction.LEFT, new pl.edu.agh.game.player.action.messages.Move(Direction.LEFT));
        messages.put(Direction.RIGHT, new pl.edu.agh.game.player.action.messages.Move(Direction.RIGHT));
    }

    private static final String NO_ARGS_MSG =
            "NO DIRECTION: please enter direction after move command (MOVE {DOWN/UP/LEFT/RIGHT})";

    @Override
    public ActionMsg execute(String[] args) {
        if (args.length == 0) {
            System.out.println(NO_ARGS_MSG);
        } else {
            if (directions.containsKey(args[0])){
                return messages.get(directions.get(args[0]));
            }
        }
        return null;
    }
}
