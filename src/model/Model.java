package model;

import controller.GameEventListener;

import java.nio.file.Paths;

//Main model class containing all internal game logic, such as starting levels, checking collisions, etc.
public class Model {
    public static final int FIELD_CELL_SIZE = 20;
    private GameEventListener eventListener;
    private GameLevel gameLevel;
    private int currentLevel = 1;

    private LevelLoader levelLoader = new LevelLoader(Paths.get(".", "/src/resources/levels.txt"));

    public void setEventListener(GameEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    public void startLevel(int level) {
        this.gameLevel = levelLoader.getLevel(level);
    }

    public void restart() {
        startLevel(currentLevel);
    }

    public void startNextLevel() {
        currentLevel = currentLevel + 1;
        startLevel(currentLevel);
    }

    public void move(Direction direction) {
        Player player = gameLevel.getPlayer();

        if (checkWallCollision(player, direction)) {
            return;
        }
        if (checkBoxCollision(direction)){
            return;
        }

        switch (direction) {
            case LEFT:
                player.move(-FIELD_CELL_SIZE, 0);
                break;
            case RIGHT:
                player.move(FIELD_CELL_SIZE, 0);
                break;
            case UP:
                player.move(0, -FIELD_CELL_SIZE);
                break;
            case DOWN:
                player.move(0, FIELD_CELL_SIZE);
        }
        checkCompletion();
    }

    public boolean checkWallCollision(CollisionObject collisionObject, Direction direction) {
        for (Wall wall : gameLevel.getWalls()){
            if(collisionObject.hasCollided(wall, direction)){
                return true;
            }
        }
        return false;
    }


    public boolean checkBoxCollision(Direction direction) {
        Player player = gameLevel.getPlayer();

        //Check what has the player collided with
        GameObject stopped = null;
        for (GameObject gameObject: gameLevel.getAll()){
            if (!(gameObject instanceof Player) && !(gameObject instanceof Base) && player.hasCollided(gameObject, direction)) {
                stopped = gameObject;
                break;
            }
        }

        //If collided with a box while moving in a direction, try to move the box one step in the same direction
        if (stopped instanceof Box) {
            Box stoppedBox = (Box)stopped;

            if (checkWallCollision(stoppedBox, direction)) {
                return true;
            }

            for (Box box : gameLevel.getBoxes()){
                if(stoppedBox.hasCollided(box, direction)) {
                    return true;
                }
            }

            switch (direction) {
                case LEFT:
                    stoppedBox.move(-FIELD_CELL_SIZE, 0);
                    break;
                case RIGHT:
                    stoppedBox.move(FIELD_CELL_SIZE, 0);
                    break;
                case UP:
                    stoppedBox.move(0, -FIELD_CELL_SIZE);
                    break;
                case DOWN:
                    stoppedBox.move(0, FIELD_CELL_SIZE);
            }
        }

        return false;
    }

    public void checkCompletion() {
        boolean yes = true;
        for (Base base : gameLevel.getBases()) {
            boolean currentyes = false;

            for (Box box: gameLevel.getBoxes()){
                if ((box.getX() == base.getX()) && (box.getY() == base.getY()))
                    currentyes = true;
            }

            if (!currentyes) {
                yes = false;
            }
        }
        if (yes) eventListener.levelCompleted(currentLevel);
    }
}