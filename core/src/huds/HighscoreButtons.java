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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.jackthegiant.GameMain;

import helpers.GameInfo;
import scenes.MainMenu;

public class HighscoreButtons {
    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Label scoreLabel, coinLabel;
    private ImageButton backBtn;

    public HighscoreButtons(GameMain game) {
        this.game = game;
        this.gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        this.stage = new Stage(gameViewport, this.game.getBatch());
        Gdx.input.setInputProcessor(this.stage);
        this.createAndPositionUIElements();
        this.stage.addActor(this.backBtn);
        this.stage.addActor(this.scoreLabel);
        this.stage.addActor(this.coinLabel);
    }

    void createAndPositionUIElements() {
        this.backBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Options Menu/Back.png"))));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;

        BitmapFont scoreFont = generator.generateFont(parameter);
        BitmapFont coinFont = generator.generateFont(parameter);

        this.scoreLabel = new Label("100", new Label.LabelStyle(scoreFont, Color.WHITE));
        this.coinLabel = new Label("100", new Label.LabelStyle(coinFont, Color.WHITE));

        this.backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                HighscoreButtons.this.game.setScreen(new MainMenu(HighscoreButtons.this.game));
            }
        });

        this.backBtn.setPosition(17, 17, Align.bottomLeft);
        this.scoreLabel.setPosition(GameInfo.WIDTH / 2f - 40f, GameInfo.HEIGHT / 2f - 120f);
        this.coinLabel.setPosition(GameInfo.WIDTH / 2f - 40f, GameInfo.HEIGHT / 2f - 215f);
    }

    public Stage getStage() {
        return this.stage;
    }
}
