package de.dogedev.ld40.ashley.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import de.dogedev.ld40.ashley.ComponentMappers;
import de.dogedev.ld40.ashley.components.DirtyComponent;
import de.dogedev.ld40.ashley.components.HiddenComponent;
import de.dogedev.ld40.ashley.components.MovementComponent;
import de.dogedev.ld40.ashley.components.PositionComponent;

/**
 * Created by Furuha on 28.01.2016.
 */
public class MovementSystem extends EntitySystem {


    private ImmutableArray<Entity> entities;

    public MovementSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, MovementComponent.class).get());
    }

    @Override
    public void removedFromEngine(Engine engine) {

    }

    Vector2 current = new Vector2();
    Vector2 target = new Vector2();

    @Override
    public void update(float deltaTime) {
        for (Entity e : entities) {
            MovementComponent mvc = ComponentMappers.movement.get(e);
            PositionComponent pvc = ComponentMappers.position.get(e);
            current.set(pvc.x, pvc.y);
            target.set(mvc.x, mvc.y);
            target.sub(current);

            if (target.len() < 40) {
                // Einheit ist angekommen
                e.add(((PooledEngine) getEngine()).createComponent(HiddenComponent.class));
                e.remove(MovementComponent.class);
                e.add(((PooledEngine) getEngine()).createComponent(DirtyComponent.class));
                continue;
            }

            float speed = 50;

            target.nor();
            target.scl(speed * deltaTime);
            pvc.x += target.x;
            pvc.y += target.y;
        }
    }


}