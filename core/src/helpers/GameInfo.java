package helpers;

public class GameInfo {
    public static final int WIDTH = 480;
    public static final int HEIGHT = 800;
    // Pixels per meter
    // -> This is necessary because libgdx considers 1 pixel is 1 meter.
    // We want to make it 100 pixels = 1 meter
    public static final float PPM = 100;

    public static final short DEFAULT = 1;
    public static final short PLAYER = 2;
    public static final short COLLECTABLE = 4;
    public static final short DESTROYED = 6;
}
