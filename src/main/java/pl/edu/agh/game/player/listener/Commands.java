package pl.edu.agh.game.player.listener;


public class Commands {
    // TODO: 5/18/17 do the commands less hardcoded
    public static final String QUIT = "quit";
    public static final String HELP = "help";
    public static final String MOVE_LEFT = "move left";
    public static final String MOVE_RIGHT = "move right";
    public static final String MOVE_UP = "move up";
    public static final String MOVE_DOWN = "move down";
    public static final String FIGHT_BEAR = "fight bear";
    public static final String FIGHT_QUILL = "fight quill";

    public static final void printHelp() {
        System.out.println(
                "HELP" +
                "* QUIT - quit the game\n" +
                "* MOVE [LEFT|RIGHT|UP|DOWN] - move in a given direction\n" +
                "* FIGHT [BEAR|QUILL] - fight with the beast\n"
                );
    }

}
