package de.dogedev.ld40.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DamageComponent implements Pool.Poolable, Component {
    public float damage = 0.0f;

    @Override
    public void reset() {
        damage = 0.0f;
    }
}
