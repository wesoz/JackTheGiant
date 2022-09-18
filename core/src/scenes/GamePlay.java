package scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.jackthegiant.GameMain;

import Player.Player;
import clouds.CloudsController;
import helpers.GameInfo;
import huds.UIHud;

public class GamePlay implements Screen, ContactListener {

    private GameMain game;
    private OrthographicCamera mainCamera;
    private Viewport gameViewport;

    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;

    private World world;

    private Sprite[] bgs;
    private float lastYPosition;
    private UIHud hud;
    private CloudsController cloudsController;
    private Player player;

    public GamePlay(GameMain game) {
        this.game = game;

        this.mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        this.mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        this.gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, this.mainCamera);

        this.box2DCamera = new OrthographicCamera();
        this.box2DCamera.setToOrtho(false, GameInfo.WIDTH / GameInfo.PPM, GameInfo.HEIGHT / GameInfo.PPM);
        this.box2DCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        this.debugRenderer = new Box2DDebugRenderer();

        this.hud = new UIHud(this.game);

        this.world = new World(new Vector2(0, -9.8f), true);
        this.world.setContactListener(this);

        this.cloudsController = new CloudsController(this.world);

        this.player = this.cloudsController.positionPlayer();

        this.createBackgrounds();
    }

    void handleInput(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.player.movePlayer(-2f);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.player.movePlayer(2f);
        } else {
            this.player.setWalking(false);
        }
    }

    void update(float dt) {
        this.handleInput(dt);
        this.moveCamera();
        this.checkBackgroundsOutOfBounds();
        this.cloudsController.setCameraY(mainCamera.position.y);
        this.cloudsController.createAndArrangeNewClouds();
        this.cloudsController.removeOffScreenCollectables();
    }

    void moveCamera() {
        this.mainCamera.position.y -= 3;
    }

    void createBackgrounds() {
        this.bgs = new Sprite[3];

        for (int i = 0; i < bgs.length; i++) {
            bgs[i] = new Sprite(new Texture("Backgrounds/Game BG.png"));
            bgs[i].setPosition(0, -(i * bgs[i].getHeight()));
            lastYPosition = Math.abs(bgs[i].getY());
        }
    }

    void drawBackgrounds() {
        for (int i = 0; i < bgs.length; i++) {
            this.game.getBatch().draw(bgs[i], bgs[i].getX(), bgs[i].getY());
        }
    }

    void checkBackgroundsOutOfBounds() {
        for (int i = 0; i < bgs.length; i++) {
            // Not using this offset will cause the top of the screen to show a
            // red(clear) line before moving the bg to the bottom because
            // the image is not fully ou of screen yet
            float outOfScreenOffset = 5f;
            if ((bgs[i].getY() - bgs[i].getHeight() / 2f - outOfScreenOffset) > mainCamera.position.y) {
                float newPosition = bgs[i].getHeight() + lastYPosition;
                bgs[i].setPosition(0, -newPosition);
                lastYPosition = Math.abs(newPosition);
            }
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
        this.cloudsController.drawClouds(this.game.getBatch());
        this.cloudsController.drawCollectables(this.game.getBatch());
        this.player.drawPlayerIdle(this.game.getBatch());
        this.player.drawPlayerAnimation(this.game.getBatch());

        this.game.getBatch().end();

        this.debugRenderer.render(world, box2DCamera.combined);

        this.game.getBatch().setProjectionMatrix(this.hud.getStage().getCamera().combined);
        this.hud.getStage().draw();

        this.game.getBatch().setProjectionMatrix(this.mainCamera.combined);
        this.mainCamera.update();

        this.player.updatePlayer();
        this.world.step(Gdx.graphics.getDeltaTime(), 6, 2);
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
        world.dispose();
        for (int i = 0; i < this.bgs.length; i++) {
            bgs[i].getTexture().dispose();
        }
        this.player.getTexture().dispose();
        this.debugRenderer.dispose();
        this.hud.getStage().dispose();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture body1, body2;
        if (contact.getFixtureA().getUserData() == "Player") {
            body1 = contact.getFixtureA();
            body2 = contact.getFixtureB();
        } else {
            body1 = contact.getFixtureB();
            body2 = contact.getFixtureA();
        }

        if (body1.getUserData() == "Player" && body2.getUserData() == "Coin") {
            body2.setUserData("Remove");
            this.cloudsController.removeCollectables();
        } else if (body1.getUserData() == "Player" && body2.getUserData() == "Life") {
            body2.setUserData("Remove");
            this.cloudsController.removeCollectables();
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
