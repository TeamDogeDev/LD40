package de.dogedev.ld40.ashley.components;

import box2dLight.Light;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class PhysicsComponent implements Pool.Poolable, Component {

    public Body body;
    public Array<Light> lights = new Array<>();

    @Override
    public void reset() {
        lights.clear();
        body = null;
    }

}
