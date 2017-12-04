package de.dogedev.ld40.ashley.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import de.dogedev.ld40.ashley.ComponentMappers;
import de.dogedev.ld40.ashley.components.BulletComponent;
import de.dogedev.ld40.ashley.components.DirtyComponent;

public class BulletSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(BulletComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for(Entity entity : entities) {
            ComponentMappers.bullet.get(entity).time  -= deltaTime;
            if(ComponentMappers.bullet.get(entity).time <= 0) {
                entity.add(((PooledEngine)getEngine()).createComponent(DirtyComponent.class));
            }
        }
    }
}
