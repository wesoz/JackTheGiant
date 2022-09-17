package clouds;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import helpers.GameInfo;

public class CloudsController {

    private final float DISTANCE_BETWEEN_CLOUDS = 250f;

    World world;
    private Array<Cloud> clouds = new Array<Cloud>();
    private float minX, maxX;
    private Random random = new Random();
    private float lastCloudPositionY;
    private float cameraY;

    public CloudsController(World world) {
        this.world = world;
        this.minX = GameInfo.WIDTH / 2f - 110;
        this.maxX = GameInfo.WIDTH / 2f + 110;
        this.createClouds();
        this.positionClouds(true);
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

    public void positionClouds(boolean firstTimeArranging) {

        while (this.clouds.get(0).getCloudName() == "Dark Cloud") {
            this.clouds.shuffle();
        }

        float positionY = 0;
        int controlX = 0;

        if (firstTimeArranging) {
            positionY = GameInfo.HEIGHT / 2f;
        } else {
            positionY = lastCloudPositionY;
        }

        for (Cloud c : this.clouds) {
            if (c.getX() == 0 && c.getY() == 0) {
                float tempX = 0;

                if (controlX == 0) {
                    tempX = this.randomBetweenNumber(maxX - 40, maxX);
                    controlX = 1;
                } else if (controlX == 1) {
                    tempX = this.randomBetweenNumber(minX + 40, minX);
                    controlX = 0;
                }

                c.setSpritePosition(tempX, positionY);
                positionY -= this.DISTANCE_BETWEEN_CLOUDS;
                lastCloudPositionY = positionY;
            }
        }
    }

    public void drawClouds(SpriteBatch batch) {
        for (Cloud c: this.clouds) {
            batch.draw(c.getTexture(), c.getX() - c.getWidth() / 2f, c.getY() - c.getHeight() / 2f);
        }
    }

    public void createAndArrangeNewClouds() {
        float cloudOutOfScreenOffset = 15f;
        for (int i = 0; i < this.clouds.size; i++) {
            if ((this.clouds.get(i).getY() - GameInfo.HEIGHT / 2f - cloudOutOfScreenOffset) > cameraY) {
                this.clouds.get(i).getTexture().dispose();
                clouds.removeIndex(i);
            }
        }

        if (this.clouds.size == 4) {
            this.createClouds();
            this.positionClouds(false);
        }
    }

    public void setCameraY(float cameraY) {
        this.cameraY = cameraY;
    }

    private float randomBetweenNumber(float min, float max) {
        return this.random.nextFloat() * (max - min) + min;
    }
}
