package Player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import helpers.GameInfo;

public class Player extends Sprite {
    private World world;
    private Body body;

    public Player(World world, float x, float y) {
        super(new Texture("Player/Player 1.png"));
        this.world = world;
        super.setPosition(x, y);
        this.createBody();
    }

    void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(super.getX() / GameInfo.PPM, super.getY() / GameInfo.PPM);

        this.body = this.world.createBody(bodyDef);

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

    public void drawPlayer(SpriteBatch batch) {
        batch.draw(this, super.getX() + super.getWidth() / 2f, super.getY() - super.getHeight() / 2f);
    }

    public void updatePlayer() {
        super.setPosition(this.body.getPosition().x * GameInfo.PPM, this.body.getPosition().y * GameInfo.PPM);
    }
}
