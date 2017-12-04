package de.dogedev.ld40.misc;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import de.dogedev.ld40.ashley.ComponentMappers;
import de.dogedev.ld40.ashley.components.DamageComponent;
import de.dogedev.ld40.ashley.components.HealthComponent;

public class AshleyB2DContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().getBody().getUserData() instanceof Entity) {
            collision((Entity) contact.getFixtureA().getBody().getUserData(), contact.getFixtureB());
        } else if (contact.getFixtureB().getBody().getUserData() instanceof Entity) {
            collision((Entity) contact.getFixtureB().getBody().getUserData(), contact.getFixtureA());
        }
    }

    private void collision(Entity first, Fixture col) {
        if(col.getBody().getUserData() instanceof Entity) {
            // 2 entities coll
            Entity second = (Entity) col.getBody().getUserData();

            HealthComponent healthComponent;
            DamageComponent damageComponent;

            if(ComponentMappers.player.has(first) && ComponentMappers.asteroid.has(second)) {
                // first: player <-> second: asteroid
                SoundManager.playShipHit();
            }

            if(ComponentMappers.player.has(second) && ComponentMappers.asteroid.has(first)) {
                // first: asteroid <-> second: player
                SoundManager.playShipHit();
            }

            if(ComponentMappers.asteroid.has(first) && ComponentMappers.asteroid.has(second)) {
                // asteroid <-> asteroid
                SoundManager.playShipHit();
            }

            if(ComponentMappers.asteroid.has(first) && ComponentMappers.bullet.has(second)) {
                // first: asteroid <-> second: bullet
                healthComponent = ComponentMappers.health.get(first);
                damageComponent = ComponentMappers.damage.get(second);

                healthComponent.health -= damageComponent.damage;

                ScoreManager.addKill();
                SoundManager.playBulletHit();
                SoundManager.playAsteroidExplosion();
            }

            if(ComponentMappers.asteroid.has(second) && ComponentMappers.bullet.has(first)) {
                // first: bullet <-> second asteroid
                healthComponent = ComponentMappers.health.get(second);
                damageComponent = ComponentMappers.damage.get(first);

                healthComponent.health -= damageComponent.damage;
                ScoreManager.addKill();
                SoundManager.playBulletHit();
                SoundManager.playAsteroidExplosion();
            }


//            if(ComponentMappers.health.has(first) && ComponentMappers.damage.has(second)) {
//                healthComponent = ComponentMappers.health.get(first);
//                damageComponent = ComponentMappers.damage.get(second);
//
//                healthComponent.health -= damageComponent.damage;
//                ScoreManager.addKill();
//            }
//
//            if(ComponentMappers.asteroid.has(second) && ComponentMappers.bullet.has(first)) {
//                // first: bullet <-> second asteroid
//                healthComponent = ComponentMappers.health.get(second);
//                damageComponent = ComponentMappers.damage.get(first);
//
//                healthComponent.health -= damageComponent.damage;
//                ScoreManager.addKill();
//            }


//            if(ComponentMappers.health.has(first) && ComponentMappers.damage.has(second)) {
//                healthComponent = ComponentMappers.health.get(first);
//                damageComponent = ComponentMappers.damage.get(second);
//
//                healthComponent.health -= damageComponent.damage;
//            }
//            if(ComponentMappers.health.has(second) && ComponentMappers.damage.has(first)) {
//                healthComponent = ComponentMappers.health.get(second);
//                damageComponent = ComponentMappers.damage.get(first);
//
//                healthComponent.health -= damageComponent.damage;
//            }

            if(ComponentMappers.bullet.has(first) && ComponentMappers.asteroid.has(second) ||
                ComponentMappers.bullet.has(second) && ComponentMappers.asteroid.has(first)){
//                ScoreManager.addKill();
            }
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
