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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.jackthegiant.GameMain;

import Player.Player;
import clouds.CloudsController;
import helpers.GameInfo;
import helpers.GameManager;
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
    private boolean touchedForTheFirstTime;
    private UIHud hud;
    private CloudsController cloudsController;
    private Player player;
    private float lastPlayerY;

    private float cameraSpeed = 10f;
    private float cameraMaxSpeed = 10f;
    private float cameraAcceleration = 10f;

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
        this.setCameraSpeed();
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

    void checkForFirstTouch() {
        if (!this.touchedForTheFirstTime) {
            if (Gdx.input.justTouched()) {
                this.touchedForTheFirstTime = true;
                GameManager.getInstance().isPaused = false;
                this.lastYPosition = this.player.getY();
            }
        }
    }

    void update(float dt) {
        this.checkForFirstTouch();;
        if (!GameManager.getInstance().isPaused) {
            this.handleInput(dt);
            this.moveCamera(dt);
            this.checkBackgroundsOutOfBounds();
            this.cloudsController.setCameraY(mainCamera.position.y);
            this.cloudsController.createAndArrangeNewClouds();
            this.cloudsController.removeOffScreenCollectables();
            this.checkPlayersBounds();
            this.countScore();
        }
    }

    void moveCamera(float delta) {

        this.mainCamera.position.y -= this.cameraSpeed * delta;
        this.cameraSpeed += this.cameraAcceleration * delta;
        if (this.cameraSpeed > this.cameraMaxSpeed) {
            this.cameraSpeed = this.cameraMaxSpeed;
        }
    }

    void setCameraSpeed() {
        if (GameManager.getInstance().gameData.isEasyDifficulty()) {
            this.cameraSpeed = 80f;
            this.cameraMaxSpeed = 100f;
        } else if (GameManager.getInstance().gameData.isEasyDifficulty()) {
            this.cameraSpeed = 100f;
            this.cameraMaxSpeed = 120f;
        } else {
            this.cameraSpeed = 120f;
            this.cameraMaxSpeed = 140f;
        }
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

    void checkPlayersBounds() {
        if (this.player.getY() - GameInfo.HEIGHT / 2f - this.player.getHeight() / 2f > this.mainCamera.position.y) {
            if (!this.player.isDead()) {
                this.playerDied();
            }
        } else if (this.player.getY() + GameInfo.HEIGHT / 2f + this.player.getHeight() / 2f < this.mainCamera.position.y) {
            if (!this.player.isDead()) {
                this.playerDied();
            }
        } else if (this.player.getX() - this.player.getWidth() / 2f > GameInfo.WIDTH || this.player.getX() + this.player.getWidth() / 2f < 0) {
            if (!this.player.isDead()) {
                this.playerDied();
            }
        }
    }

    void countScore() {
        if (this.lastPlayerY > this.player.getY()) {
            this.hud.incrementScore(1);
            this.lastPlayerY = this.player.getY();
        }
    }

    void playerDied() {
        GameManager.getInstance().isPaused = true;
        this.hud.decrementLife();
        this.player.setDead(true);
        this.player.setPosition(1000f, 1000f);
        if (GameManager.getInstance().lifeScore < 0) {
            GameManager.getInstance().checkForNewHighscore();

            this.hud.createGameOverPanel();

            RunnableAction run = new RunnableAction();
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    GamePlay.this.game.setScreen(new MainMenu(GamePlay.this.game));
                }
            });
            SequenceAction sa = new SequenceAction();
            sa.addAction(Actions.delay(3f));
            sa.addAction(Actions.fadeOut(1f));
            sa.addAction(run);

            this.hud.getStage().addAction(sa);
        } else {
            RunnableAction run = new RunnableAction();
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    GamePlay.this.game.setScreen(new GamePlay(GamePlay.this.game));
                }
            });
            SequenceAction sa = new SequenceAction();
            sa.addAction(Actions.delay(3f));
            sa.addAction(Actions.fadeOut(1f));
            sa.addAction(run);

            this.hud.getStage().addAction(sa);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        this.update(delta);

        ScreenUtils.clear(0, 0, 0, 1);

        this.game.getBatch().begin();

        this.drawBackgrounds();
        this.cloudsController.drawClouds(this.game.getBatch());
        this.cloudsController.drawCollectables(this.game.getBatch());
        this.player.drawPlayerIdle(this.game.getBatch());
        this.player.drawPlayerAnimation(this.game.getBatch());

        this.game.getBatch().end();

        // this.debugRenderer.render(world, this.box2DCamera.combined);

        this.game.getBatch().setProjectionMatrix(this.hud.getStage().getCamera().combined);
        this.hud.getStage().draw();
        this.hud.getStage().act();

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
            this.hud.incrementCoin();
            this.cloudsController.removeCollectables();
        } else if (body1.getUserData() == "Player" && body2.getUserData() == "Life") {
            body2.setUserData("Remove");
            this.hud.incrementLife();
            this.cloudsController.removeCollectables();
        }

        if (body1.getUserData() == "Player" && body2.getUserData() == "Dark Cloud") {
            if (!this.player.isDead()) {
                this.playerDied();
            }
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
