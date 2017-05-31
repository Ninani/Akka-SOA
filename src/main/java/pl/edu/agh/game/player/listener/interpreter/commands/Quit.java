package pl.edu.agh.game.player.listener.interpreter.commands;

import pl.edu.agh.game.player.action.messages.ActionMsg;
import pl.edu.agh.game.player.listener.interpreter.commands.Command;

/**
 * QUIT
 */
public class Quit implements Command {

    private static final String returnMessage =
            "Bye!";

    @Override
    public ActionMsg execute(String[] args) {
        System.out.println(returnMessage);
        return null;
    }
}
