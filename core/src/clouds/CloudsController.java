package clouds;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import helpers.GameInfo;

public class CloudsController {

    private final float DISTANCE_BETWEEN_CLOUDS = 250f;

    World world;
    private Array<Cloud> clouds = new Array<Cloud>();

    public CloudsController(World world) {
        this.world = world;
        this.createClouds();
        this.positionClouds();
    }

    void createClouds() {
        for (int i = 0; i < 2; i++) {
            this.clouds.add(new Cloud(this.world, "Dark Cloud"));
        }

        int index = 1;

        for (int i = 0; i < 6; i++) {
            this.clouds.add(new Cloud(this.world, "Cloud " + index));
            index++;

            if (index > 3) {
                index = 1;
            }

            this.clouds.shuffle();
        }
    }

    public void positionClouds() {

        while (this.clouds.get(0).getCloudName() == "Dark Cloud") {
            this.clouds.shuffle();
        }

        float positionY = GameInfo.HEIGHT / 2f;
        float tempX = GameInfo.WIDTH /2f;

        for (Cloud c : this.clouds) {
            c.setSpritePosition(tempX, positionY);
            positionY -= this.DISTANCE_BETWEEN_CLOUDS;
        }
    }

    public void drawClouds(SpriteBatch batch) {
        for (Cloud c: this.clouds) {
            batch.draw(c.getTexture(), c.getX() - c.getWidth() / 2f, c.getY() - c.getHeight() / 2f);
        }

    }
}
