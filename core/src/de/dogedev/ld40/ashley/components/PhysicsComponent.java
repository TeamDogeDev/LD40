package de.dogedev.ld40.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

public class PhysicsComponent implements Pool.Poolable, Component {

    public Body body;

    @Override
    public void reset() {
        body = null;
    }

    @Override
    public String toString() {
        return body.getPosition().x + " , " + body.getPosition().y ;
    }
}
