package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

//Reads data from a given file and parses it to create a next GameLevel object.
//Example file is provided in resources package and contains 60 levels. After completing all of them goes back to level 1.
public class LevelLoader {
    private Path levels;

    public LevelLoader(Path levels) {
        this.levels = levels;
    }

    public GameLevel getLevel(int level) {
        if(level>60) level %= 60;
        Set<Box> boxes = new HashSet<>();
        Set<Wall> walls = new HashSet<>();
        Set<Base> bases = new HashSet<>();
        Player player = new Player(0,0);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(levels.toString()));
            while (true) {
                String input = reader.readLine();
                if (("Maze: " + level).equals(input))
                    break;
            }
            reader.readLine();
            int width = Integer.parseInt(reader.readLine().split(" ")[2]);
            int height = Integer.parseInt(reader.readLine().split(" ")[2]);
            reader.readLine();
            reader.readLine();
            reader.readLine();
            for(int y = 0; y < height; y++) {
                String in = reader.readLine();
                char[] array = in.toCharArray();
                for (int x = 0; x < width; x++) {
                    char character = array[x];
                    switch (character) {
                        case ' ': //Empty cell
                            break;
                        case '@': //The player
                            player = new Player(x * Model.FIELD_CELL_SIZE + Model.FIELD_CELL_SIZE / 2, y * Model.FIELD_CELL_SIZE + Model.FIELD_CELL_SIZE / 2);
                            break;
                        case 'X': //A wall segment
                            walls.add(new Wall(x * Model.FIELD_CELL_SIZE + Model.FIELD_CELL_SIZE / 2, y * Model.FIELD_CELL_SIZE + Model.FIELD_CELL_SIZE / 2));
                            break;
                        case '.': //An empty home
                            bases.add(new Base(x * Model.FIELD_CELL_SIZE + Model.FIELD_CELL_SIZE / 2, y * Model.FIELD_CELL_SIZE + Model.FIELD_CELL_SIZE / 2));
                            break;
                        case '*': //A box not at home
                            boxes.add(new Box(x * Model.FIELD_CELL_SIZE + Model.FIELD_CELL_SIZE / 2, y * Model.FIELD_CELL_SIZE + Model.FIELD_CELL_SIZE / 2));
                            break;
                        case '&': //A box at home
                            boxes.add(new Box(x * Model.FIELD_CELL_SIZE + Model.FIELD_CELL_SIZE / 2, y * Model.FIELD_CELL_SIZE + Model.FIELD_CELL_SIZE / 2));
                            bases.add(new Base(x * Model.FIELD_CELL_SIZE + Model.FIELD_CELL_SIZE / 2, y * Model.FIELD_CELL_SIZE + Model.FIELD_CELL_SIZE / 2));
                            break;
                    }
                }
            }
        }
        catch (IOException e) {
            System.out.println("Error reading file");
        }
        return new GameLevel(walls, boxes, bases, player);
    }
}