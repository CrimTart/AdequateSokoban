package controller;

import view.View;
import model.Model;
import model.Direction;
import model.GameLevel;

public class Controller implements GameEventListener {
    private View view;
    private Model model;

    public Controller() {
        model = new Model();
        model.restart();
        model.setEventListener(this);
        view = new View(this);
        view.init();
        view.setEventListener(this);
    }

    public void move(Direction direction){
        model.move(direction);
        view.update();
    }

    public void restart(){
        model.restart();
        view.update();
    }

    public void startNextLevel(){
        model.startNextLevel();
        view.update();
    }

    public void levelCompleted(int level) {
        view.completed(level);
    }

    public static void main(String[] args) {
        Controller controller = new Controller();
    }

    public GameLevel getGameObjects() {
        return model.getGameLevel();
    }
}