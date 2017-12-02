package de.dogedev.ld40.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import de.dogedev.ld40.ashley.ComponentMappers;
import de.dogedev.ld40.ashley.components.DirtyComponent;
import de.dogedev.ld40.ashley.systems.DebugUISystem;
import de.dogedev.ld40.ashley.systems.MovementSystem;
import de.dogedev.ld40.ashley.systems.PhysicsSystem;
import de.dogedev.ld40.ashley.systems.RenderSystem;
import de.dogedev.ld40.misc.AshleyB2DContactListener;
import de.dogedev.ld40.misc.EntityFactory;

import static de.dogedev.ld40.Statics.ashley;

public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private OrthographicCamera debugCamera;
    private RenderSystem renderSystem;
    private ImmutableArray<Entity> dirtyEntities;
    private Box2DDebugRenderer debugRenderer;
    private World world;

    public GameScreen() {
        init();
    }

    public void init() {
        ashley.removeAllEntities();

        camera = new OrthographicCamera();
        camera.zoom = 1f;
        camera.setToOrtho(false, 1280, 720);

        debugCamera = new OrthographicCamera();
        debugCamera.zoom = camera.zoom / PhysicsSystem.PIXEL_PER_METER;
        debugCamera.setToOrtho(false, 1280, 720);

        ashley.addSystem(new MovementSystem(2));
        renderSystem = new RenderSystem(camera, 5);
        ashley.addSystem(renderSystem);
        ashley.addSystem(new DebugUISystem(camera, 6));

        dirtyEntities = ashley.getEntitiesFor(Family.all(DirtyComponent.class).get());

        world = new World(new Vector2(0, -9.81f), false);
        ashley.addSystem(new PhysicsSystem(world, 1));

        world.setContactListener(new AshleyB2DContactListener());

        EntityFactory.createEnemy(world, new Vector2(50, 100), MathUtils.PI / 1.323f);
        Entity enemy = EntityFactory.createEnemy(world, new Vector2(50, 50), MathUtils.PI / 1.323f);
        ComponentMappers.physics.get(enemy).body.applyLinearImpulse(0,-1000, 0, 0, true);

        // Create our body definition
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vector2(0, 1));
        Body groundBody = world.createBody(groundBodyDef);
//
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(camera.viewportWidth, 1.0f);
        groundBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();

        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ashley.update(delta);
        debugRenderer.render(world, debugCamera.combined);


        // remove dirty entities
        if (dirtyEntities.size() > 0) {
            for (Entity entity : dirtyEntities) {
                ashley.removeEntity(entity);
            }
        }
    }


    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
    }

    @Override
    public void dispose() {
//        batch.dispose();
    }
}
