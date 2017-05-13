package pl.edu.agh.game.model.map;

import pl.edu.agh.game.model.Enemy;

public class Location {

    private boolean blank;
    private int id;
    private Action action;
    private Enemy enemy;

    public Location(boolean blank, int id, Action action) {
        this.blank = blank;
        this.id = id;
        this.action = action;
    }

    public boolean isBlank() {
        return blank;
    }

    public int getId() {
        return id;
    }

    public void setBlank(boolean blank) {
        this.blank = blank;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }
}
