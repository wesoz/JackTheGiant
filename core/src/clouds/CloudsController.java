package clouds;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import Player.Player;
import collectables.Collectable;
import helpers.GameInfo;

public class CloudsController {

    private final float DISTANCE_BETWEEN_CLOUDS = 250f;

    World world;
    private Array<Cloud> clouds = new Array<>();
    private Array<Collectable> collectables = new Array<>();

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
                    tempX = this.randomBetweenNumber(maxX - 10, maxX);
                    controlX = 1;
                } else if (controlX == 1) {
                    tempX = this.randomBetweenNumber(minX + 10, minX);
                    controlX = 0;
                }

                c.setSpritePosition(tempX - c.getWidth() / 2f, positionY - c.getHeight() / 2f);
                positionY -= this.DISTANCE_BETWEEN_CLOUDS;
                lastCloudPositionY = positionY;

                if (!firstTimeArranging && c.getCloudName() != "Dark Cloud") {
                    int rand = random.nextInt(10);
                    if (rand > 5) {
                        int randomCollectable = random.nextInt(2);
                        if (randomCollectable == 0) {
                            Collectable collectable = new Collectable(this.world, "Life");
                            collectable.setCollectablePosition(c.getX() + c.getWidth() / 2f - collectable.getWidth() / 2f, c.getY() + c.getHeight() / 2f + 40f);
                            this.collectables.add(collectable);
                        } else {
                            Collectable collectable = new Collectable(this.world, "Coin");
                            collectable.setCollectablePosition(c.getX() + c.getWidth() / 2f - collectable.getWidth() / 2f, c.getY() + c.getHeight() / 2f + 40f);
                            this.collectables.add(collectable);
                        }
                    }
                }
            }
        }
    }

    public void drawClouds(SpriteBatch batch) {
        for (Cloud c: this.clouds) {
            c.draw(batch);
        }
    }

    public void drawCollectables(SpriteBatch batch) {
        for (Collectable c: this.collectables) {
            c.updateCollectable();
            c.draw(batch);
        }
    }

    public void removeCollectables() {
        for (int i = 0; i < this.collectables.size; i++) {
            if (this.collectables.get(i).getFixture().getUserData() == "Remove") {
                this.collectables.get(i).changeFilter();
                this.collectables.get(i).getTexture().dispose();
                this.collectables.removeIndex(i);
            }
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

    public void removeOffScreenCollectables() {
        for (int i = 0; i < this.collectables.size; i++) {
            if ((this.collectables.get(i).getY() - GameInfo.HEIGHT / 2f - 15) > this.cameraY) {
                this.collectables.get(i).getTexture().dispose();
                this.collectables.removeIndex(i);
            }
        }
    }

    public void setCameraY(float cameraY) {
        this.cameraY = cameraY;
    }

    public Player positionPlayer() {
        Cloud topCloud = clouds.get(0);
        return new Player(this.world, topCloud.getX() + topCloud.getWidth() / 2f, topCloud.getY() + topCloud.getHeight() / 2f + 100f);
    }

    private float randomBetweenNumber(float min, float max) {
        return this.random.nextFloat() * (max - min) + min;
    }
}
