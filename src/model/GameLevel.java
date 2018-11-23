package model;

import java.util.HashSet;
import java.util.Set;

//A container for all GameObjects of a given level. Filled with data from a file by LevelLoader.
public class GameLevel {
    private Set<Wall> walls;
    private Set<Box> boxes;
    private Set<Base> bases;
    private Player player;

    public GameLevel(Set<Wall> walls, Set<Box> boxes, Set<Base> bases, Player player) {
        this.walls = walls;
        this.boxes = boxes;
        this.bases = bases;
        this.player = player;
    }

    public Set<GameObject> getAll() {
        Set<GameObject> all = new HashSet<>();

        all.addAll(getWalls());
        all.addAll(getBoxes());
        all.addAll(getBases());
        all.add(getPlayer());

        return all;
    }

    public Set<Wall> getWalls() {
        return walls;
    }

    public Set<Box> getBoxes() {
        return boxes;
    }

    public Set<Base> getBases() {
        return bases;
    }

    public Player getPlayer() {
        return player;
    }
}