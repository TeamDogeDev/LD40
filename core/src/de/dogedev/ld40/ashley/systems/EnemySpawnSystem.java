package de.dogedev.ld40.ashley.systems;

import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import de.dogedev.ld40.misc.EntityFactory;

public class EnemySpawnSystem extends IntervalSystem {

    private World world;

    public EnemySpawnSystem(float interval, int priority, World world) {
        super(interval, priority);
        this.world = world;
    }

    @Override
    protected void updateInterval() {
        // Spawn
        EntityFactory.createEnemy(world, new Vector2(10, 50), 45 * MathUtils.degreesToRadians, 500);

    }
}
