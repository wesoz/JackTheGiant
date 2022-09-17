package scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.jackthegiant.GameMain;

import helpers.GameInfo;

public class GamePlay implements Screen {

    private GameMain game;
    private OrthographicCamera mainCamera;
    private Viewport gameViewport;
    private Sprite[] bgs;

    public GamePlay(GameMain game) {
        this.game = game;

        this.mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        this.mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        this.gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, this.mainCamera);



        this.createBackgrounds();
    }

    void update(float dt) {
        moveCamera();
    }

    void moveCamera() {
        this.mainCamera.position.y -= 1;
    }

    void createBackgrounds() {
        this.bgs = new Sprite[3];

        for (int i = 0; i < bgs.length; i++) {
            bgs[i] = new Sprite(new Texture("Backgrounds/Game BG.png"));
            bgs[i].setPosition(0, -(i * bgs[i].getHeight()));
        }
    }

    void drawBackgrounds() {
        for (int i = 0; i < bgs.length; i++) {
            this.game.getBatch().draw(bgs[i], bgs[i].getX(), bgs[i].getY());
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        this.update(delta);

        ScreenUtils.clear(1, 0, 0, 1);

        this.game.getBatch().begin();

        this.drawBackgrounds();

        this.game.getBatch().end();

        this.game.getBatch().setProjectionMatrix(this.mainCamera.combined);
        this.mainCamera.update();
    }

    @Override
    public void resize(int width, int height) {

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

    }
}
