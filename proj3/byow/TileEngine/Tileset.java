package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static TETile AVATAR = new TETile('@', Color.white, Color.black, "you");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.white, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");

    public static final TETile pixelGrass = new TETile('❀', Color.magenta, Color.pink, "pixel grass",
            "/Users/ivanyun/Desktop/cs61bl/su23-s173/proj3/byow/TileEngine/tile_0001.png");

    public static final TETile pixelWall = new TETile('❀', Color.magenta, Color.pink, "pixel wall",
            "/Users/ivanyun/Desktop/cs61bl/su23-s173/proj3/byow/TileEngine/tile_0043.png");

    //@source https://kenney.nl/assets/tiny-town
    public static final TETile pixelNothing = new TETile('❀', Color.magenta, Color.pink, "pixel nothing",
            "/Users/ivanyun/Desktop/cs61bl/su23-s173/proj3/byow/TileEngine/tile_0039.png");
    //@source https://kenney.nl/assets/tiny-town
    public static final TETile pixelBomb = new TETile('❀', Color.magenta, Color.pink, "pixel bomb",
            "/Users/ivanyun/Desktop/cs61bl/su23-s173/proj3/byow/TileEngine/tile_0105.png");
    //@source https://kenney.nl/assets/tiny-town
    public static final TETile pixelTarget = new TETile('❀', Color.magenta, Color.pink, "pixel target",
            "/Users/ivanyun/Desktop/cs61bl/su23-s173/proj3/byow/TileEngine/tile_0095.png");
    //@source https://kenney.nl/assets/tiny-town
    public static final TETile pixelMushroom = new TETile('❀', Color.magenta, Color.pink, "pixel mushroom",
            "/Users/ivanyun/Desktop/cs61bl/su23-s173/proj3/byow/TileEngine/tile_0029.png");
    //@source https://kenney.nl/assets/tiny-town
    public static final TETile key = new TETile('❀', Color.magenta, Color.pink, "pixel key",
            "/Users/ivanyun/Desktop/cs61bl/su23-s173/proj3/byow/TileEngine/tile_0117.png");

    //"@source https://kenney.nl/assets/tiny-town"
    public static final TETile coins = new TETile('❀', Color.magenta, Color.pink, "pixel coin",
            "/Users/ivanyun/Desktop/cs61bl/su23-s173/proj3/byow/TileEngine/tile_0093.png");
}


