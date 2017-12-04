package de.dogedev.ld40.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by elektropapst on 23.04.2017.
 */
public class BulletComponent implements Component, Pool.Poolable {

    public float time = 0.5f;

    @Override
    public void reset() {
        time = 0.5f;
    }
}