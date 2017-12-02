package de.dogedev.ld40.screens;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import de.dogedev.ld40.ashley.ComponentMappers;
import de.dogedev.ld40.ashley.components.DirtyComponent;
import de.dogedev.ld40.ashley.components.PhysicsComponent;
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
    private RayHandler rayHandler;

    PhysicsComponent physicsComponent;


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

        world = new World(new Vector2(0, 0), false);

        RayHandler.setGammaCorrection(true);
//        RayHandler.useDiffuseLight(true);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.01f, 0.01f, 0.01f, 0.5f);
        rayHandler.setBlurNum(3);
        rayHandler.setShadows(true);

        ashley.addSystem(new PhysicsSystem(world, 1));

        world.setContactListener(new AshleyB2DContactListener());

        for (int i = 0; i < 5; i++) {
            EntityFactory.createEnemy(world, new Vector2(MathUtils.random(0, Gdx.graphics.getWidth() / PhysicsSystem.PIXEL_PER_METER), MathUtils.random(0, Gdx.graphics.getHeight() / PhysicsSystem.PIXEL_PER_METER)), MathUtils.random(0, MathUtils.degreesToRadians * 180), rayHandler);
        }
        Entity enemy = EntityFactory.createEnemy(world, new Vector2(50, 50), 0, rayHandler);
        physicsComponent = ComponentMappers.physics.get(enemy);

        // Create our body definition
//        BodyDef groundBodyDef = new BodyDef();
//        groundBodyDef.position.set(new Vector2(0, 1));
//        Body groundBody = world.createBody(groundBodyDef);
////
//        PolygonShape groundBox = new PolygonShape();
//        groundBox.setAsBox(camera.viewportWidth, 1.0f);
//        groundBody.createFixture(groundBox, 0.0f);
//        groundBox.dispose();


        debugRenderer = new Box2DDebugRenderer();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ashley.update(delta);
        debugRenderer.render(world, debugCamera.combined);
        rayHandler.setCombinedMatrix(debugCamera);
        rayHandler.updateAndRender();


        if(Gdx.input.isTouched()){
            Vector3 unproject = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            float x = unproject.x / PhysicsSystem.PIXEL_PER_METER;
            float y = unproject.y / PhysicsSystem.PIXEL_PER_METER;

            Array<Body> bodies = new Array<>();
            world.getBodies(bodies);
            for(Body body: bodies){
                Vector2 force;
                Vector2 sub = body.getPosition().sub(new Vector2(x, y));
                force = sub.nor().scl(80000);
                body.applyForce(force, new Vector2(x, y), true);
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            physicsComponent.body.applyForceToCenter(new Vector2(0, 400).rotateRad(physicsComponent.body.getAngle()), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
//            physicsComponent.body.applyForceToCenter(new Vector2(-400, 0), true);
            physicsComponent.body.applyAngularImpulse(40, true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {

            physicsComponent.body.applyForceToCenter(new Vector2(0, -400).rotateRad(physicsComponent.body.getAngle()), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
//            physicsComponent.body.applyForceToCenter(new Vector2(400, 0), true);
            physicsComponent.body.applyAngularImpulse(-40, true);
        }
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
        rayHandler.dispose();
//        batch.dispose();
    }
}
