package collectables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import helpers.GameInfo;

public class Collectable extends Sprite {
    private World world;
    private Body body;
    private String name;
    private Fixture fixture;

    public Collectable(World world, String name) {
        super(new Texture("Collectables/" + name + ".png"));
        this.world = world;
        this.name = name;
    }

    void createCollectableBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(
                (super.getX() + super.getWidth() / 2f ) / GameInfo.PPM,
                (super.getY() + super.getHeight() / 2f ) / GameInfo.PPM
        );

        this.body = this.world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((super.getWidth() / 2f) / GameInfo.PPM, (super.getHeight() / 2f) / GameInfo.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameInfo.COLLECTABLE;
        fixtureDef.isSensor = true;

        this.fixture = this.body.createFixture(fixtureDef);
        this.fixture.setUserData(this.name);

        shape.dispose();
    }

    public void setCollectablePosition(float x, float y) {
        super.setPosition(x, y);
        this.createCollectableBody();
    }

    public void updateCollectable() {
        float bodyCenterX = (this.body.getPosition().x * GameInfo.PPM) - super.getWidth() / 2f;
        float bodyCenterY = (this.body.getPosition().y * GameInfo.PPM) - super.getHeight() / 2f;
        super.setPosition(bodyCenterX, bodyCenterY);
    }

    public void changeFilter() {
        Filter filter = new Filter();
        filter.categoryBits = GameInfo.DESTROYED;
        this.fixture.setFilterData(filter);
    }

    public Fixture getFixture() {
        return this.fixture;
    }
}
