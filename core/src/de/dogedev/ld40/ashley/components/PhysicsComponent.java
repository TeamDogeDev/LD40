package de.dogedev.ld40.ashley.components;

import box2dLight.ConeLight;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

public class PhysicsComponent implements Pool.Poolable, Component {

    public Body body;
    public ConeLight coneLight;

    @Override
    public void reset() {
        body = null;
    }

}
