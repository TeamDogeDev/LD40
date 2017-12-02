package de.dogedev.ld40.misc;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;

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
