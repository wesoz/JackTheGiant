package scenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.jackthegiant.GameMain;

import helpers.GameInfo;
import huds.HighscoreButtons;

public class Highscore implements Screen {
    private GameMain game;
    private OrthographicCamera mainCamera;
    private Viewport gameViewport;
    private Texture bg;

    private HighscoreButtons btns;

    public Highscore(GameMain game) {
        this.game = game;
        this.mainCamera = new OrthographicCamera();
        this.mainCamera.setToOrtho(false, GameInfo.WIDTH, GameInfo.HEIGHT);
        this.mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);
        this.gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, this.mainCamera);
        this.bg = new Texture("Backgrounds/Highscore BG.png");
        this.btns = new HighscoreButtons(this.game);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        this.game.getBatch().begin();

        this.game.getBatch().draw(this.bg, 0, 0);

        this.game.getBatch().end();

        this.game.getBatch().setProjectionMatrix(this.btns.getStage().getCamera().combined);
        this.btns.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        this.gameViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.bg.dispose();
        this.btns.getStage().dispose();
    }
}
