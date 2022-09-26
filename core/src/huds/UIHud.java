package huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.jackthegiant.GameMain;

import helpers.GameInfo;
import helpers.GameManager;
import scenes.MainMenu;

public class UIHud {
    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Image coinImg, lifeImg, scoreImg, pausePanel;
    private Label coinLabel, lifeLabel, scoreLabel;

    private ImageButton pauseBtn, resumeBtn, quitBtn;

    public UIHud(GameMain game) {
        this.game = game;
        this.gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        this.stage = new Stage(gameViewport, this.game.getBatch());
        Gdx.input.setInputProcessor(this.stage);

        if (GameManager.getInstance().gameStartedFromMainMenu) {
            GameManager.getInstance().gameStartedFromMainMenu = false;
            GameManager.getInstance().lifeScore = 2;
            GameManager.getInstance().coinScore = 0;
            GameManager.getInstance().score = 0;
        }

        this.createLabels();
        this.createImages();
        this.createButtonAndAddListener();

        Table lifeAndCoinTable = new Table();
        lifeAndCoinTable.top().left();
        lifeAndCoinTable.setFillParent(true);
        lifeAndCoinTable.add(this.lifeImg).padLeft(10).padTop(10);
        lifeAndCoinTable.add(this.lifeLabel).padLeft(5).padTop(10);

        lifeAndCoinTable.row();

        lifeAndCoinTable.add(this.coinImg).padLeft(10).padTop(10);
        lifeAndCoinTable.add(this.coinLabel).padLeft(10).padTop(10);

        Table scoreTable = new Table();
        scoreTable.top().right();
        scoreTable.setFillParent(true);
        scoreTable.add(this.scoreImg).padRight(20).padTop(15);
        scoreTable.row();
        scoreTable.add(this.scoreLabel).padRight(20).padTop(15);

        this.stage.addActor(lifeAndCoinTable);
        this.stage.addActor(scoreTable);
        this.stage.addActor(this.pauseBtn);
    }

    void createLabels() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;

        BitmapFont font = generator.generateFont(parameter);
        this.coinLabel = new Label("x" + GameManager.getInstance().coinScore, new Label.LabelStyle(font, Color.WHITE));
        this.lifeLabel = new Label("x" + GameManager.getInstance().lifeScore, new Label.LabelStyle(font, Color.WHITE));
        this.scoreLabel = new Label(String.valueOf(GameManager.getInstance().score), new Label.LabelStyle(font, Color.WHITE));
    }

    void createImages() {
        this.coinImg = new Image(new Texture("collectables/Coin.png"));
        this.lifeImg = new Image(new Texture("collectables/Life.png"));
        this.scoreImg = new Image(new Texture("Buttons/Gameplay/Score.png"));
    }

    void createButtonAndAddListener() {
        this.pauseBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Gameplay/Pause.png"))));

        this.pauseBtn.setPosition(470, 17, Align.bottomRight);

        this.pauseBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!GameManager.getInstance().isPaused) {
                    GameManager.getInstance().isPaused = true;
                    UIHud.this.createPausePanel();
                }
            }
        });
    }

    void createPausePanel() {
        this.pausePanel = new Image(new Texture("Buttons/Pause/Pause Panel.png"));
        this.resumeBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Pause/Resume.png"))));
        this.quitBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Pause/Quit 2.png"))));

        this.pausePanel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, Align.center);
        this.resumeBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 50f, Align.center);
        this.quitBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 80f, Align.center);

        this.resumeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.getInstance().isPaused = false;
                UIHud.this.removePausePanel();
            }
        });
        this.quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UIHud.this.game.setScreen(new MainMenu(UIHud.this.game));
            }
        });

        this.stage.addActor(this.pausePanel);
        this.stage.addActor(this.resumeBtn);
        this.stage.addActor(this.quitBtn);
    }

    void removePausePanel() {
        this.pausePanel.remove();
        this.resumeBtn.remove();
        this.quitBtn.remove();
    }

    public void createGameOverPanel() {
        Image gameOverPanel = new Image(new Texture("Buttons/Pause/Show Score.png"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;

        BitmapFont font = generator.generateFont(parameter);
        Label endScore = new Label(String.valueOf(GameManager.getInstance().score), new Label.LabelStyle(font, Color.WHITE));
        Label endCoinScore = new Label(String.valueOf(GameManager.getInstance().coinScore), new Label.LabelStyle(font, Color.WHITE));

        gameOverPanel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, Align.center);

        endScore.setPosition(GameInfo.WIDTH / 2f + 40f, GameInfo.HEIGHT / 2f + 75f, Align.center);
        endCoinScore.setPosition(GameInfo.WIDTH / 2f - 30f, GameInfo.HEIGHT / 2f - 50f, Align.center);

        this.stage.addActor(gameOverPanel);
        this.stage.addActor(endScore);
        this.stage.addActor(endCoinScore);

    }

    public void incrementScore(int score) {
        GameManager.getInstance().score += score;
        this.scoreLabel.setText(String.valueOf(GameManager.getInstance().score));
    }

    public void incrementCoin() {
        GameManager.getInstance().coinScore++;
        this.coinLabel.setText("x" + GameManager.getInstance().coinScore);
        this.incrementScore(200);
    }

    public void incrementLife() {
        GameManager.getInstance().lifeScore++;
        this.lifeLabel.setText("x" + GameManager.getInstance().lifeScore);
        this.incrementScore(300);
    }

    public void decrementLife() {
        GameManager.getInstance().lifeScore--;
        if(GameManager.getInstance().lifeScore >= 0) {
            this.lifeLabel.setText("x" + GameManager.getInstance().lifeScore);
        }
    }

    public Stage getStage() {
        return this.stage;
    }
}
