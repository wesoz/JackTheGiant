package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public class GameManager {
    private static GameManager ourInstance = new GameManager();

    public GameData gameData;
    private Json json = new Json();
    private FileHandle fileHandle = Gdx.files.local("bin/GameData.json");

    public boolean gameStartedFromMainMenu, isPaused = true;
    public int lifeScore, coinScore, score;

    private GameManager() { }

    public void initializeGameData() {
        if (!this.fileHandle.exists()) {
            this.gameData = new GameData();

            this.gameData.setHighscore(0);
            this.gameData.setCoinHighscore(0);

            this.gameData.setEasyDifficulty(false);
            this.gameData.setMediumDifficulty(true);
            this.gameData.setHardDifficulty(false);

            this.gameData.setMusicOn(false);

            this.saveData();
        } else {
            loadData();
        }
    }

    public void saveData() {
        if (this.gameData != null) {
            this.fileHandle.writeString(Base64Coder.encodeString(this.json.prettyPrint(this.gameData)), false);
        }
    }

    public void loadData() {
        this.gameData = this.json.fromJson(GameData.class, Base64Coder.decodeString(this.fileHandle.readString()));
    }

    public void checkForNewHighscore() {
        int oldHighscore = this.gameData.getHighscore();
        int oldCoinHighscore = this.gameData.getCoinHighscore();

        if (oldHighscore < this.score) {
            gameData.setHighscore(this.score);
        }

        if (oldCoinHighscore < this.coinScore) {
            gameData.setCoinHighscore(this.coinScore);
        }

        this.saveData();
    }

    public static GameManager getInstance() { return ourInstance; }
}
