package pl.edu.agh.game.player.listener.interpreter.commands;

import pl.edu.agh.game.player.action.messages.ActionMsg;

/**
 * HELP
 */
public class Help implements Command {

    private static final String returnMessage =
            ">>>> HELP <<<<\n" +
            "* QUIT - quit the game\n" +
            "* MOVE [LEFT|RIGHT|UP|DOWN] - move in a given direction\n" +
            "* FIGHT WITH BEAST - fight with the beast\n" +
            "* SHOW [ACTIONS|DIRECTIONS] - show possible actions/directions";

    @Override
    public ActionMsg execute(String[] args) {
        System.out.println(returnMessage);
        return null;
    }
}
