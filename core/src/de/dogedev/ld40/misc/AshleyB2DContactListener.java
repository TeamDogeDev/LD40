package de.dogedev.ld40.misc;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import de.dogedev.ld40.ashley.ComponentMappers;

public class AshleyB2DContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().getBody().getUserData() instanceof Entity) {
            col((Entity) contact.getFixtureA().getBody().getUserData(), contact.getFixtureB());
        } else if (contact.getFixtureB().getBody().getUserData() instanceof Entity) {
            col((Entity) contact.getFixtureB().getBody().getUserData(), contact.getFixtureA());
        }
    }

    private void col(Entity e, Fixture col) {
        if(col.getBody().getUserData() instanceof Entity) {
            // 2 entities coll
            Entity userData = (Entity) col.getBody().getUserData();
            if(ComponentMappers.health.has(userData)) {
                ComponentMappers.health.get(userData).health-=0.5;
            }
            if(ComponentMappers.health.has(e)) {
                ComponentMappers.health.get(e).health-=0.5;
            }

            System.out.println("Collision with entity");

        } else {
            // entity mit welt
            System.out.println("collision with world");
        }
    }


    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
