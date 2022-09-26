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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.jackthegiant.GameMain;

import helpers.GameInfo;
import helpers.GameManager;
import scenes.MainMenu;

public class OptionsButtons {
    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private ImageButton easy, medium, hard, backBtn;
    private Image sign;

    public OptionsButtons(GameMain game) {
        this.game = game;
        this.gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        this.stage = new Stage(gameViewport, this.game.getBatch());
        Gdx.input.setInputProcessor(this.stage);
        this.createAndPositionUIElements();
        this.addAllListeners();
        this.stage.addActor(this.easy);
        this.stage.addActor(this.medium);
        this.stage.addActor(this.hard);
        this.stage.addActor(this.backBtn);
        this.stage.addActor(this.sign);
    }

    void createAndPositionUIElements() {
        this.easy = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Options Menu/Easy.png"))));
        this.medium = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Options Menu/Medium.png"))));
        this.hard = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Options Menu/Hard.png"))));
        this.sign = new Image(new Texture("Buttons/Options Menu/Check Sign.png"));
        this.backBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Options Menu/Back.png"))));

        this.backBtn.setPosition(17, 17, Align.bottomLeft);
        this.easy.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 40, Align.center);
        this.medium.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 40, Align.center);
        this.hard.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 120, Align.center);

        this.positionTheSign();
    }

    void addAllListeners() {
        this.backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                OptionsButtons.this.game.setScreen(new MainMenu(OptionsButtons.this.game));
            }
        });
        this.easy.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                OptionsButtons.this.changeDifficulty(0);
            }
        });
        this.medium.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                OptionsButtons.this.changeDifficulty(1);
            }
        });
        this.hard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                OptionsButtons.this.changeDifficulty(2);
            }
        });
    }

    void changeDifficulty(int difficulty) {
        GameManager.getInstance().gameData.setEasyDifficulty(difficulty == 0);
        GameManager.getInstance().gameData.setMediumDifficulty(difficulty == 1);
        GameManager.getInstance().gameData.setHardDifficulty(difficulty == 2);

        GameManager.getInstance().saveData();

        this.positionTheSign();
    }

    void positionTheSign() {
        if (GameManager.getInstance().gameData.isEasyDifficulty()) {
            this.sign.setPosition(GameInfo.WIDTH / 2f + 76,OptionsButtons.this.easy.getY() + 13);
        } else if (GameManager.getInstance().gameData.isMediumDifficulty()) {
            this.sign.setPosition(GameInfo.WIDTH / 2f + 76,OptionsButtons.this.medium.getY() + 13);
        } else {
            this.sign.setPosition(GameInfo.WIDTH / 2f + 76,OptionsButtons.this.hard.getY() + 13);
        }
    }

    public Stage getStage() {
        return this.stage;
    }
}
