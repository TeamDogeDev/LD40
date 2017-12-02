package de.dogedev.ld40.ashley.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

import java.text.DecimalFormat;

/**
 * Created by Furuha on 09.04.2016.
 */
public class DebugUISystem extends EntitySystem implements Disposable {

    private final GLProfiler glProfiler;
    private SpriteBatch batch = new SpriteBatch();
    private BitmapFont font;
    private OrthographicCamera camera;
    private DecimalFormat floatFormat = new DecimalFormat("#.##");

    public DebugUISystem(OrthographicCamera camera, int priority) {
        super(priority);
        this.camera = camera;
        font = new BitmapFont();
        glProfiler = new GLProfiler(Gdx.graphics);
        glProfiler.enable();
    }

    @Override
    public void update(float deltaTime) {

        batch.begin();
        font.setColor(Color.WHITE);
        Vector3 pos = getMousePosInGameWorld();
        if(camera != null)debugLine(260, "mouse x=" + (int) (pos.x) );
        if(camera != null)debugLine(240, "mouse y=" + (int) (pos.y) );
        if(camera != null)debugLine(220, "cam x=" + floatFormat.format(camera.position.x));
        if(camera != null)debugLine(200, "cam y=" + floatFormat.format(camera.position.y));
        debugLine(180, "entities=" + getEngine().getEntities().size());
        if(camera != null)debugLine(160, "zoom=" + camera.zoom);
        debugLine(100, "DC=" + glProfiler.getDrawCalls());
        debugLine(80, "TXB=" + glProfiler.getTextureBindings());
        debugLine(60, "FPS=" + Gdx.graphics.getFramesPerSecond());
        batch.end();

        glProfiler.reset();

    }

    private Vector3 mouse = new Vector3();
    public Vector3 getMousePosInGameWorld() {
        return camera.unproject(mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    public void debugLine(float pos, String text){
        font.draw(batch, text, 1070, pos, 200, Align.right, false);
    }

    @Override
    public void dispose() {
        font.dispose();
        glProfiler.disable();
    }


}