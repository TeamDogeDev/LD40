package de.dogedev.ld40.ashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by Furuha on 28.01.2016.
 */
public class InputSystem extends EntitySystem  {


    private final OrthographicCamera camera;
    private final InputMultiplexer inputMultiplexer;

    public InputSystem(OrthographicCamera camera, int priority) {
        super(priority);
        this.camera = camera;
        inputMultiplexer = new InputMultiplexer();
    }

    @Override
    public void addedToEngine (Engine engine) {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void removedFromEngine (Engine engine) {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void update (float deltaTime) {

    }


}