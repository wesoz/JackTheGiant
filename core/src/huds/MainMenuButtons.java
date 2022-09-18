package huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.jackthegiant.GameMain;

import helpers.GameInfo;
import scenes.GamePlay;

public class MainMenuButtons {
    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private ImageButton playBtn;
    private ImageButton highscoreBtn;
    private ImageButton optionsBtn;
    private ImageButton quitBtn;
    private ImageButton musicBtn;

    public MainMenuButtons(GameMain game) {
        this.game = game;
        this.gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        this.stage = new Stage(gameViewport, this.game.getBatch());
        Gdx.input.setInputProcessor(this.stage);
        this.createAndPositionButtons();
        this.addAllListeners();
        this.stage.addActor(this.playBtn);
        this.stage.addActor(this.highscoreBtn);
        this.stage.addActor(this.optionsBtn);
        this.stage.addActor(this.quitBtn);
        this.stage.addActor(this.musicBtn);
    }

    void createAndPositionButtons() {
        this.playBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Main Menu/Start Game.png"))));
        this.highscoreBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Main Menu/Highscore.png"))));
        this.optionsBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Main Menu/Options.png"))));
        this.quitBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Main Menu/Quit.png"))));
        this.musicBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Main Menu/Music On.png"))));

        this.playBtn.setPosition(GameInfo.WIDTH / 2f - 80, GameInfo.HEIGHT / 2f + 50, Align.center);
        this.highscoreBtn.setPosition(GameInfo.WIDTH / 2f - 60, GameInfo.HEIGHT / 2f - 20, Align.center);
        this.optionsBtn.setPosition(GameInfo.WIDTH / 2f - 40, GameInfo.HEIGHT / 2f - 90, Align.center);
        this.quitBtn.setPosition(GameInfo.WIDTH / 2f - 20, GameInfo.HEIGHT / 2f - 160, Align.center);
        this.musicBtn.setPosition(GameInfo.WIDTH / 2f - 13, 13, Align.center);
    }

    void addAllListeners() {
        this.playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuButtons.this.game.setScreen(new GamePlay(MainMenuButtons.this.game));
            }
        });
        this.highscoreBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        this.optionsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        this.quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        this.musicBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
    }

    public Stage getStage() {
        return this.stage;
    }
}
