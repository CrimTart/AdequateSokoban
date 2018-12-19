package model;

import controller.GameEventListener;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

//Main model class containing all internal game logic, such as starting levels, checking collisions, etc.

public class Model {
    public static final int FIELD_CELL_SIZE = 20;
    public static final int SAVED_STATES_AMOUNT = 15;
    private GameEventListener eventListener;
    private GameLevel gameLevel;
    private int currentLevel = 1;

    private LevelLoader levelLoader = new LevelLoader(getClass().getClassLoader().getResource("levels.txt").getPath());
    private ArrayDeque<GameLevel> previousStates = new ArrayDeque<>();

    public void setEventListener(GameEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getSteps() {
        return gameLevel.getSteps();
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
        gameLevel.setSteps(0);
    }

    public void move(Direction direction) {
        previousStates.push(currentState());
        Player player = gameLevel.getPlayer();

        if (checkWallCollision(player, direction)) {
            previousStates.pop();
            return;
        }
        if (checkBoxCollision(direction)){
            previousStates.pop();
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

        gameLevel.setSteps(gameLevel.getSteps() + 1);
        if (previousStates.size() > SAVED_STATES_AMOUNT)
            previousStates.pollLast();
        checkCompletion();
    }

    public GameLevel currentState() {
        Set<Wall> walls = new HashSet<>();
        Set<Box> boxes = new HashSet<>();
        Set<Base> bases = new HashSet<>();
        Player player = new Player(gameLevel.getPlayer().getX(), gameLevel.getPlayer().getY());
        for (Wall w : gameLevel.getWalls()) {walls.add(new Wall(w.getX(), w.getY()));}
        for (Box bo : gameLevel.getBoxes()) {boxes.add(new Box(bo.getX(), bo.getY()));}
        for (Base ba : gameLevel.getBases()) {bases.add(new Base(ba.getX(), ba.getY()));}
        return new GameLevel(gameLevel.getNumber(), gameLevel.getSteps(), walls, boxes, bases, player);
    }

    public void previousStep() {
        if (!previousStates.isEmpty()) gameLevel = previousStates.pop();
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

            //Can't move if box is up against a wall
            if (checkWallCollision(stoppedBox, direction)) {
                return true;
            }

            //Can't move more than one box at a time
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

    public void saveGame() {
        try {
            String path = getClass().getClassLoader().getResource("save.txt").getPath();
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(gameLevel);
            out.flush();
            out.close();
            fileOut.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGame() {
        previousStates.clear();
        GameLevel loaded;
        try {
            String path = getClass().getClassLoader().getResource("save.txt").getPath();
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            loaded = (GameLevel)in.readObject();
            this.gameLevel = loaded;
            this.currentLevel = loaded.getNumber();
            in.close();
            fileIn.close();
        }
        catch (ClassNotFoundException c) {
            System.out.println("GameLevel class not found!");
            c.printStackTrace();
        }
        catch (Exception i) {
            i.printStackTrace();
        }
    }
}