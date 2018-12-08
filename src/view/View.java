package view;

import controller.Controller;
import controller.GameEventListener;
import model.GameLevel;

import javax.swing.*;

public class View extends JFrame {
    private Controller controller;
    private Field field;

    public View(Controller controller) {
        this.controller = controller;
    }

    public void setEventListener(GameEventListener eventListener){
        this.field.setEventListener(eventListener);
    }

    public void init() {
        field = new Field(this);
        add(field);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setTitle("Adequate Sokoban");
        setVisible(true);
    }

    public void update() {
        this.field.repaint();
    }

    public GameLevel getGameObjects() {
        return controller.getGameObjects();
    }

    public void completed(int level) {
        update();
        JOptionPane.showMessageDialog(null, "Level " + level + " Completed!", "Level Complete", JOptionPane.INFORMATION_MESSAGE);
        controller.startNextLevel();
    }

    public void showHelpDialog() {
        JOptionPane.showMessageDialog(null, "Movement: Arrow keys\nRestart level: R\nSave Game: F5\nLoad Game: F8", "Controls", JOptionPane.INFORMATION_MESSAGE);
    }
}
