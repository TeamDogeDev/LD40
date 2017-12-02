package de.dogedev.ld40.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by elektropapst on 22.04.2017.
 */
public class PositionComponent implements Component, Pool.Poolable {

    public float x;
    public float y;
    public float rotation;

    @Override
    public void reset() {
        x = 0;
        y = 0;
        rotation = 0;
    }
}