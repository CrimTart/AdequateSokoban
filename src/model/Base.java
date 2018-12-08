package model;

import java.awt.*;

//All bases must be filled with boxes to complete the level.

public class Base extends GameObject{
    public Base(int x, int y){
        super(x,y);
        setWidth(Model.FIELD_CELL_SIZE / 10);
        setHeight(Model.FIELD_CELL_SIZE / 10);
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(Color.ORANGE);

        int leftUpperCornerX = getX() - getWidth() / 2;
        int leftUpperCornerY = getY() - getHeight() / 2;

        graphics.drawRect(leftUpperCornerX, leftUpperCornerY, getWidth(), getHeight());
        graphics.fillRect(leftUpperCornerX, leftUpperCornerY, getWidth(), getHeight());
    }
}