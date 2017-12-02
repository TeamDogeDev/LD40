package de.dogedev.ld40.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class HealthComponent implements Pool.Poolable, Component{
    public float health = 1;
    @Override
    public void reset() {
        health = 1;
    }
}
