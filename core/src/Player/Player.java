package Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import helpers.GameInfo;

public class Player extends Sprite {
    private World world;
    private Body body;

    private TextureAtlas playerAtlas;
    private Animation<TextureRegion> animation;
    private float elapsedTime;

    private boolean isWalking;

    public Player(World world, float x, float y) {
        super(new Texture("Player/Player 1.png"));
        this.world = world;
        super.setPosition(x, y);
        this.createBody();
        this.playerAtlas = new TextureAtlas("Player Animation/Player Animation.atlas");
    }

    void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(super.getX() / GameInfo.PPM, super.getY() / GameInfo.PPM);

        this.body = this.world.createBody(bodyDef);
        this.body.setFixedRotation(true);

        PolygonShape shape = new PolygonShape();
        float boxWidthOffset = 20f;
        shape.setAsBox((super.getWidth() / 2f - boxWidthOffset) / GameInfo.PPM, (super.getHeight() / 2f) / GameInfo.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 4; // the mass of the body
        fixtureDef.friction = 2; // will make player slide on surfaces
        fixtureDef.shape = shape;

        Fixture fixture = this.body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void drawPlayerIdle(SpriteBatch batch) {
        if (!this.isWalking) {
            batch.draw(this, super.getX() - super.getWidth() / 2f, super.getY() - super.getHeight() / 2f);
        }
    }

    public void drawPlayerAnimation(SpriteBatch batch) {
        if (this.isWalking) {
            elapsedTime += Gdx.graphics.getDeltaTime();

            Array<TextureAtlas.AtlasRegion> frames = this.playerAtlas.getRegions();

            for (TextureRegion frame: frames) {
                if (this.body.getLinearVelocity().x < 0 && !frame.isFlipX()) {
                    frame.flip(true, false);
                } else if (this.body.getLinearVelocity().x > 0 && frame.isFlipX()) {
                    frame.flip(true, false);
                }
            }

            this.animation = new Animation(1f/10f, this.playerAtlas.getRegions());

            batch.draw(this.animation.getKeyFrame(elapsedTime, true),
                    super.getX() - super.getWidth() / 2f, super.getY() - super.getHeight() / 2f);
        }
    }

    public void movePlayer(float x) {
        if (x < 0 && !this.isFlipX()) {
            this.flip(true, false);
        } else if (x > 0 && this.isFlipX()) {
            this.flip(true, false);
        }
        this.setWalking(true);
        this.body.setLinearVelocity(x, body.getLinearVelocity().y);
    }

    public void updatePlayer() {
        super.setPosition(this.body.getPosition().x * GameInfo.PPM, this.body.getPosition().y * GameInfo.PPM);
    }

    public void setWalking(boolean isWalking) {
        this.isWalking = isWalking;
    }
}
