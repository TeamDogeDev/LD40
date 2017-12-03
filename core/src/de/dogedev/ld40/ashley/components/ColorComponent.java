package de.dogedev.ld40.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by elektropapst on 23.04.2017.
 */
public class ColorComponent implements Component, Pool.Poolable {

    public Color color;

    @Override
    public void reset() {
        color = Color.WHITE;
    }
}