package clouds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import helpers.GameInfo;

public class Cloud extends Sprite {
    private World world;
    private Body body;
    private String cloudName;

    public Cloud(World world, String cloudName) {
        super(new Texture("Clouds/" + cloudName + ".png"));

        this.world = world;
        this.cloudName = cloudName;
    }

    void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        float centerXOffset = 40f;
        float centerX = (super.getX() - centerXOffset) / GameInfo.PPM;
        float centerY = (super.getY()) / GameInfo.PPM;
        bodyDef.position.set(centerX, centerY);

        this.body = this.world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        float boxWidthOffset = 20f;
        shape.setAsBox((super.getWidth() / 2f - boxWidthOffset) / GameInfo.PPM, (super.getHeight() / 2f) / GameInfo.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        Fixture fixture = this.body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void setSpritePosition(float x, float y) {
        super.setPosition(x, y);
        this.createBody();
    }

    public String getCloudName() {
        return cloudName;
    }
}
