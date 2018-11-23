package model;

//All the objects that can collide (don't allow other such objects to pass through).
public abstract class CollisionObject extends GameObject{
    public CollisionObject(int x, int y){
        super(x,y);
    }

    public boolean hasCollided(GameObject gameObject, Direction direction) {
        boolean result = false;

        switch (direction) {
            case LEFT:
                if (getX() - Model.FIELD_CELL_SIZE == gameObject.getX() && getY() == gameObject.getY())
                    result = true;
                break;
            case RIGHT:
                if (getX() + Model.FIELD_CELL_SIZE == gameObject.getX() && getY() == gameObject.getY())
                    result = true;
                break;
            case UP:
                if (getX() == gameObject.getX() && getY() - Model.FIELD_CELL_SIZE == gameObject.getY())
                    result = true;
                break;
            case DOWN:
                if (getX() == gameObject.getX() && getY() + Model.FIELD_CELL_SIZE == gameObject.getY())
                    result = true;
                break;
        }
        return result;
    }
}