package pl.edu.agh.game.player.listener.interpreter;

import pl.edu.agh.game.player.action.messages.ActionMsg;
import pl.edu.agh.game.player.listener.interpreter.commands.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandInterpreter {

    private static final Map<String, Command> commandStorage = new HashMap<>();
    static {
        commandStorage.put(CommandType.QUIT, new Quit());
        commandStorage.put(CommandType.HELP, new Help());
        commandStorage.put(CommandType.SHOW, new Show());
        commandStorage.put(CommandType.MOVE, new Move());
        commandStorage.put(CommandType.FIGHT, new Fight());
        commandStorage.put(CommandType.WRONG_COMMAND, new WrongCommand());
    }

    public ActionMsg executeCommand(String inputLine){
        String[] words = standardizeInput(inputLine);
        String[] args = extractArgs(words);

        if (Objects.equals(words[0], CommandType.QUIT)){
            return commandStorage.get(CommandType.QUIT).execute(args);
        }

        if (commandStorage.containsKey(words[0])) {
            return commandStorage.get(words[0]).execute(args);
        } else {
            return commandStorage.get(CommandType.WRONG_COMMAND).execute(args);
        }
    }

    private String[] standardizeInput(String inputLine){
        String[] words = inputLine.split(" ");
        for (int i=0; i<words.length; i++) {
            words[i] = words[i].toLowerCase();
        }
        return words;
    }

    private String[] extractArgs(String[] words) {
        String[] args = new String[words.length-1];
        System.arraycopy(words, 1, args, 0, args.length);
        return args;
    }

}
