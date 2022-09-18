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

public class UIHud {
    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Image coinImg, lifeImg, scoreImg;
    private Label coinLabel, lifeLabel, scoreLabel;

    private ImageButton pauseBtn;

    public UIHud(GameMain game) {
        this.game = game;
        this.gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        this.stage = new Stage(gameViewport, this.game.getBatch());
        Gdx.input.setInputProcessor(this.stage);
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
        this.coinLabel = new Label("x0", new Label.LabelStyle(font, Color.WHITE));
        this.lifeLabel = new Label("x2", new Label.LabelStyle(font, Color.WHITE));
        this.scoreLabel = new Label("100", new Label.LabelStyle(font, Color.WHITE));
    }

    void createImages() {
        this.coinImg = new Image(new Texture("Collectables/Coin.png"));
        this.lifeImg = new Image(new Texture("Collectables/Life.png"));
        this.scoreImg = new Image(new Texture("Buttons/Gameplay/Score.png"));
    }

    void createButtonAndAddListener() {
        this.pauseBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Gameplay/Pause.png"))));

        this.pauseBtn.setPosition(470, 17, Align.bottomRight);

        this.pauseBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
    }

    public Stage getStage() {
        return this.stage;
    }
}