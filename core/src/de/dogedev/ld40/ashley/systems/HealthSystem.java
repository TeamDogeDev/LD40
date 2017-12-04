package de.dogedev.ld40.ashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import de.dogedev.ld40.Statics;
import de.dogedev.ld40.ashley.ComponentMappers;
import de.dogedev.ld40.ashley.components.DirtyComponent;
import de.dogedev.ld40.ashley.components.HealthComponent;
import de.dogedev.ld40.ashley.components.PositionComponent;
import de.dogedev.ld40.assets.ParticlePool;

public class HealthSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(HealthComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for(Entity entity : entities) {
            if(ComponentMappers.health.get(entity).health <= 0.0f) {
                PositionComponent pc = ComponentMappers.position.get(entity);
                ParticlePool.ParticleType type = ParticlePool.ParticleType.COLD_EXPLOSION;
                if(MathUtils.randomBoolean()) {
                    type = ParticlePool.ParticleType.HOT_EXPLOSION;
                }
                Statics.particle.createParticleAt(type, pc.x, pc.y);
                entity.add(Statics.ashley.createComponent(DirtyComponent.class));
            }
        }
    }
}
