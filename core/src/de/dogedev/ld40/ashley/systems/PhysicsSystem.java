package de.dogedev.ld40.ashley.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import de.dogedev.ld40.ashley.ComponentMappers;
import de.dogedev.ld40.ashley.components.DirtyComponent;
import de.dogedev.ld40.ashley.components.PhysicsComponent;
import de.dogedev.ld40.ashley.components.PositionComponent;

public class PhysicsSystem extends EntitySystem {

    public static final int PIXEL_PER_METER = 10;

    private final World world;
    private ImmutableArray<Entity> entities;
    private static final float MAX_STEP_TIME = 1 / 60f;
    private static float accumulator = 0f;


    public PhysicsSystem(World world, int priority) {
        super(priority);
        this.world = world;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PhysicsComponent.class, PositionComponent.class).get());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

    @Override
    public void update(float deltaTime) {

        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        if (accumulator >= MAX_STEP_TIME) {
            world.step(MAX_STEP_TIME, 6, 2);
            accumulator -= MAX_STEP_TIME;

            PhysicsComponent physicsComponent;
            PositionComponent positionComponent;

            for (Entity entity : entities) {

                physicsComponent = ComponentMappers.physics.get(entity);
                positionComponent = ComponentMappers.position.get(entity);
                Vector2 position = physicsComponent.body.getPosition();

                if(physicsComponent.coneLight != null){
                    physicsComponent.coneLight.setDirection(physicsComponent.body.getAngle() * MathUtils.radiansToDegrees + 90);
                    physicsComponent.coneLight.setPosition(physicsComponent.body.getPosition());
                }

                if(position.x > Gdx.graphics.getWidth() / PIXEL_PER_METER +5) {
                    if(ComponentMappers.weak.has(entity)){
                        entity.add(((PooledEngine)getEngine()).createComponent(DirtyComponent.class));
                    } else {
                        physicsComponent.body.setTransform(-5, position.y, physicsComponent.body.getAngle());
                    }
                }
                if(position.x < -5) {
                    if(ComponentMappers.weak.has(entity)){
                        entity.add(((PooledEngine)getEngine()).createComponent(DirtyComponent.class));
                    } else {
                        physicsComponent.body.setTransform(Gdx.graphics.getWidth() / PIXEL_PER_METER+5, position.y, physicsComponent.body.getAngle());
                    }
                }
                if(position.y > Gdx.graphics.getHeight() / PIXEL_PER_METER +5) {
                    if(ComponentMappers.weak.has(entity)){
                        entity.add(((PooledEngine)getEngine()).createComponent(DirtyComponent.class));
                    } else {
                        physicsComponent.body.setTransform(position.x, -5, physicsComponent.body.getAngle());
                    }
                }
                if(position.y < -5) {
                    if(ComponentMappers.weak.has(entity)){
                        entity.add(((PooledEngine)getEngine()).createComponent(DirtyComponent.class));
                    } else {
                        physicsComponent.body.setTransform(position.x, Gdx.graphics.getHeight() / PIXEL_PER_METER +5, physicsComponent.body.getAngle());
                    }
                }

                positionComponent.x = position.x * PIXEL_PER_METER;
                positionComponent.y = position.y * PIXEL_PER_METER;

                positionComponent.rotation = physicsComponent.body.getAngle() * MathUtils.radiansToDegrees;
            }
        }
    }
}
