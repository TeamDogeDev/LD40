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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import de.dogedev.ld40.ashley.ComponentMappers;
import de.dogedev.ld40.ashley.components.DirtyComponent;
import de.dogedev.ld40.ashley.components.PhysicsComponent;
import de.dogedev.ld40.ashley.systems.*;
import de.dogedev.ld40.misc.AshleyB2DContactListener;
import de.dogedev.ld40.misc.EntityFactory;
import de.dogedev.ld40.misc.Shake;

import static de.dogedev.ld40.Statics.ashley;


public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private OrthographicCamera debugCamera;
    private RenderSystem renderSystem;
    private ImmutableArray<Entity> dirtyEntities;
    private Box2DDebugRenderer debugRenderer;
    private World world;
    private RayHandler rayHandler;

    private Shake shake;

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
//        ashley.addSystem(new EnemySpawnSystem(1, 3, world));
        ashley.addSystem(new HealthSystem());

        world.setContactListener(new AshleyB2DContactListener());

        EntityFactory.createBase(world, new Vector2(Gdx.graphics.getWidth() / PhysicsSystem.PIXEL_PER_METER / 2, Gdx.graphics.getHeight() / PhysicsSystem.PIXEL_PER_METER / 2), 0);
        EntityFactory.createEnemy(world, new Vector2(10, 50), 45 * MathUtils.degreesToRadians, 500);

        Entity player = EntityFactory.createPlayer(world, new Vector2(50, 50), 0, rayHandler);
        physicsComponent = ComponentMappers.physics.get(player);

        debugRenderer = new Box2DDebugRenderer(true, true, false, true, true, true);

        shake = new Shake();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shake.update(delta, camera, new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2));

        ashley.update(delta);
        debugRenderer.render(world, debugCamera.combined);
        rayHandler.setCombinedMatrix(debugCamera);
        rayHandler.updateAndRender();


//        if(Gdx.input.isTouched()){
//            Vector3 unproject = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
//            float x = unproject.x / PhysicsSystem.PIXEL_PER_METER;
//            float y = unproject.y / PhysicsSystem.PIXEL_PER_METER;
//
//            Array<Body> bodies = new Array<>();
//            world.getBodies(bodies);
//            for(Body body: bodies){
//                Vector2 force;
//                Vector2 sub = body.getPosition().sub(new Vector2(x, y));
//                force = sub.nor().scl(80000);
//                body.applyForce(force, new Vector2(x, y), true);
//            }
//        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Vector2 angleDiff = new Vector2(2, 0);
            angleDiff.setAngle(physicsComponent.body.getAngle() * MathUtils.radiansToDegrees + 90);
            EntityFactory.createBullet(world, physicsComponent.body.getPosition().add(angleDiff), physicsComponent.body.getAngle(), 50, rayHandler);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            physicsComponent.body.applyForceToCenter(new Vector2(0, 400).rotateRad(physicsComponent.body.getAngle()), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//            physicsComponent.body.applyForceToCenter(new Vector2(-400, 0), true);
            physicsComponent.body.applyAngularImpulse(5, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            physicsComponent.body.applyForceToCenter(new Vector2(-400, 0), true);
//            physicsComponent.body.applyAngularImpulse(5, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {

            physicsComponent.body.applyForceToCenter(new Vector2(0, -400).rotateRad(physicsComponent.body.getAngle()), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//            physicsComponent.body.applyForceToCenter(new Vector2(400, 0), true);
            physicsComponent.body.applyAngularImpulse(-5, true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            shake.shake(1);
            if (Gdx.input.isKeyPressed(Input.Keys.E)) {
                physicsComponent.body.applyForceToCenter(new Vector2(400, 0), true);
//            physicsComponent.body.applyAngularImpulse(-5, true);
            }
            // remove dirty entities
            if (dirtyEntities.size() > 0) {
                for (Entity entity : dirtyEntities) {
                    if (ComponentMappers.physics.has(entity)) {
                        world.destroyBody(ComponentMappers.physics.get(entity).body);
                    }
                    ashley.removeEntity(entity);
                }
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