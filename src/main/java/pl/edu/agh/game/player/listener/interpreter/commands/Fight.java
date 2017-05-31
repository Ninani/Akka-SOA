package pl.edu.agh.game.player.listener.interpreter.commands;

import pl.edu.agh.game.player.action.messages.ActionMsg;


/**
 * FIGHT WITH BEAST
 */
public class Fight implements Command {

    private static final String WRONG_INPUT_FORMAT_MSG =
            "WRONG COMMAND FORMAT: please enter the fight command in the following format: FIGHT WITH BEAST";

    private pl.edu.agh.game.player.action.messages.Fight fightMessage = new pl.edu.agh.game.player.action.messages.Fight();

    @Override
    public ActionMsg execute(String[] args) {
        if (args.length < 2 ) {
            System.out.println(WRONG_INPUT_FORMAT_MSG);
        } else if (!args[0].equals("with") && !args[1].equals("beast")) {
            System.out.println(WRONG_INPUT_FORMAT_MSG);
        } else {
            return fightMessage;
        }
        return null;
    }

}

