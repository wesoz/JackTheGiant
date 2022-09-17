package helpers;

public class GameInfo {
    public static final int WIDTH = 480;
    public static final int HEIGHT = 800;
    // Pixels per meter
    // -> This is necessary because libgdx considers 1 pixel is 1 meter.
    // We want to make it 1 pixel = 100 meters
    public static final int PPM = 100;
}
