package helpers;

public class GameManager {
    private static GameManager ourInstance = new GameManager();

    public boolean gameStartedFromMainMenu, isPaused = true;
    public int lifeScore, coinScore, score;

    private GameManager() { }

    public static GameManager getInstance() { return ourInstance; }
}
