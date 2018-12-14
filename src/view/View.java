package view;

import controller.Controller;
import controller.GameEventListener;
import model.GameLevel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame {
    private Controller controller;
    private JSplitPane splitPane;
    private Field field;
    private JPanel uiElements;
    private JLabel infoLabel;

    public View(Controller controller) {
        this.controller = controller;
    }

    public void setEventListener(GameEventListener eventListener){
        this.field.setEventListener(eventListener);
    }

    public void init() {
        splitPane = new JSplitPane();
        field = new Field(this);
        uiElements = new JPanel();

        setSize(new Dimension(560, 500));
        getContentPane().setLayout(new GridLayout());
        getContentPane().add(splitPane);

        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setTopComponent(field);
        splitPane.setBottomComponent(uiElements);

        infoLabel = new JLabel("LEVEL #" + String.valueOf(controller.getCurrentLevel()) +
                "         TOTAL STEPS: " + String.valueOf(controller.getSteps()) + "    ");
        JButton stepBackButton = new JButton("UNDO");
        stepBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.previousStep();
                field.requestFocus();
            }
        });
        JButton restartButton = new JButton("RESTART LEVEL");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.restart();
                field.requestFocus();
            }
        });
        JLabel helpLabel = new JLabel("        PRESS F1 FOR HELP");
        uiElements.add(infoLabel);
        uiElements.add(stepBackButton);
        uiElements.add(restartButton);
        uiElements.add(helpLabel);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Adequate Sokoban");
        setVisible(true);
    }

    public void update() {
        this.field.repaint();
        this.infoLabel.setText("LEVEL #" + String.valueOf(controller.getCurrentLevel()) +
                "         TOTAL STEPS: " + String.valueOf(controller.getSteps()) + "    ");
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
        JOptionPane.showMessageDialog(null, "Push all boxes on bases to complete the level\nMovement: Arrow keys\nSave Game: F5\nLoad Game: F9", "Help", JOptionPane.INFORMATION_MESSAGE);
    }
}
