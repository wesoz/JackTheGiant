package clouds;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class CloudsController {
    World world;
    private Array<Cloud> clouds = new Array<Cloud>();

    public CloudsController(World world) {
        this.world = world;
        this.createClouds();
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
}
