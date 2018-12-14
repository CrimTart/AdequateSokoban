package view;

import controller.GameEventListener;
import model.Direction;
import model.GameObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Set;

public class Field extends JPanel {
    private View view;
    private GameEventListener eventListener;

    public Field(View view){
        this.view = view;
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);
    }

    public void setEventListener(GameEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, view.getWidth(), view.getHeight() - 20);

        Set<GameObject> gameObjectSet = view.getGameObjects().getAll();

        for (GameObject gameObject : gameObjectSet) {
            gameObject.draw(g);
        }
    }

    public class KeyHandler extends KeyAdapter {
        private Field field;

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case (KeyEvent.VK_LEFT):
                    eventListener.move(Direction.LEFT);
                    break;
                case (KeyEvent.VK_RIGHT):
                    eventListener.move(Direction.RIGHT);
                    break;
                case (KeyEvent.VK_UP):
                    eventListener.move(Direction.UP);
                    break;
                case (KeyEvent.VK_DOWN):
                    eventListener.move(Direction.DOWN);
                    break;
                case (KeyEvent.VK_F1):
                    eventListener.showHelpDialog();
                    break;
                case (KeyEvent.VK_F5):
                    eventListener.saveGame();
                    break;
                case (KeyEvent.VK_F9):
                    eventListener.loadGame();
                    break;
            }
        }
    }
}
