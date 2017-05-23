package pl.edu.agh.game.player.listener.interpreter.commands;

import pl.edu.agh.game.player.action.messages.ActionMsg;

public class WrongCommand implements Command {

    private static final String returnMessage =
            "Wrong command! Type `help` to get a list of available commands.";

    @Override
    public ActionMsg execute(String[] args) {
        System.out.println(returnMessage);
        return null;
    }
}
