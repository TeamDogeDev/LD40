package de.dogedev.ld40.ashley.systems;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import de.dogedev.ld40.misc.EntityFactory;

public class EnemySpawnSystem extends EntitySystem {

    private float interval;
    private float accumulator;

    private World world;
    private RayHandler rayHandler;

    public EnemySpawnSystem(float interval, int priority, World world, RayHandler rayHandler) {
        super(priority);
        this.interval = interval;
        this.world = world;
        this.rayHandler = rayHandler;
    }

    @Override
    public void update(float deltaTime) {
        accumulator += deltaTime;

        while (accumulator >= interval) {
            accumulator -= interval;
            updateInterval();
        }
    }

    public void setUpdateInterval(float interval) {
        this.interval = interval;
    }

    private void updateInterval() {
        int xPosition = 0;
        int yPosition = 0;

        float angle = MathUtils.random(0, MathUtils.PI2);


        boolean horizontal = MathUtils.randomBoolean();
        if(horizontal) {
            boolean top = MathUtils.randomBoolean();

            if(!top) yPosition = Gdx.graphics.getHeight();
            xPosition = MathUtils.random(0, Gdx.graphics.getWidth());
        } else {
            boolean left = MathUtils.randomBoolean();

            if(!left) xPosition = Gdx.graphics.getWidth();
            yPosition = MathUtils.random(0, Gdx.graphics.getHeight());
        }

        xPosition /= PhysicsSystem.PIXEL_PER_METER;
        yPosition /= PhysicsSystem.PIXEL_PER_METER;

        EntityFactory.createEnemy(world, new Vector2(xPosition, yPosition), angle, 500, rayHandler);

    }

    public void decreaseUpdateInterval() {
//        interval -= 0.1;
    }
}
