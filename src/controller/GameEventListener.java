package controller;

import model.Direction;

public interface GameEventListener {
    void move(Direction direction);
    void restart();
    void startNextLevel();
    void levelCompleted(int level);
    void showHelpDialog();
    void saveGame();
    void loadGame();
    void previousStep();
}