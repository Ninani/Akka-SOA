package pl.edu.agh.game.player.listener.interpreter.commands;

import pl.edu.agh.game.player.action.messages.ActionMsg;

public interface Command {
    ActionMsg execute(String[] args);
}
