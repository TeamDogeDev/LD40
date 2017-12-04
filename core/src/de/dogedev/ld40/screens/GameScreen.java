package de.dogedev.ld40.screens;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import de.dogedev.ld40.LDGame;
import de.dogedev.ld40.Statics;
import de.dogedev.ld40.ashley.ComponentMappers;
import de.dogedev.ld40.ashley.components.DirtyComponent;
import de.dogedev.ld40.ashley.components.PhysicsComponent;
import de.dogedev.ld40.ashley.systems.*;
import de.dogedev.ld40.assets.enums.ShaderPrograms;
import de.dogedev.ld40.assets.enums.Textures;
import de.dogedev.ld40.misc.AshleyB2DContactListener;
import de.dogedev.ld40.misc.EntityFactory;
import de.dogedev.ld40.misc.ScoreManager;
import de.dogedev.ld40.misc.SoundManager;
import de.dogedev.ld40.overlays.AbstractOverlay;
import de.dogedev.ld40.overlays.ParticleOverlay;

import static de.dogedev.ld40.Statics.ashley;


public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private OrthographicCamera debugCamera;
    private RenderSystem renderSystem;
    private ImmutableArray<Entity> dirtyEntities;
    private Box2DDebugRenderer debugRenderer;
    private World world;
    private RayHandler rayHandler;
    private RayHandler rayHandler2;

    private Texture background;
    private Batch backgroundBatch;
    private ShaderProgram backgroundShader;

    PhysicsComponent physicsComponent;
    private float lastSpeed;
    private ConeLight engineLight;

    private Array<AbstractOverlay> overlays = new Array<>();
    private PointLight redFog;
    private PointLight yellowFog;
    private PointLight blueFog;
    private PointLight greenFog;


    public GameScreen() {
        init();
    }

    public void init() {



        ashley.removeAllEntities();
        ashley.getSystems().iterator().forEachRemaining(system -> ashley.removeSystem(system));

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
        ashley.addSystem(new OverlayRenderSystem(7));

        dirtyEntities = ashley.getEntitiesFor(Family.all(DirtyComponent.class).get());

        world = new World(new Vector2(0, 0), false);

        RayHandler.setGammaCorrection(true);
//        RayHandler.useDiffuseLight(true);


        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.01f, 0.01f, 0.01f, 0.5f);
        rayHandler.setBlurNum(3);
        rayHandler.setShadows(true);

        rayHandler2 = new RayHandler(new World(new Vector2(0, 0), true));
        rayHandler2.setShadows(false);
        rayHandler2.setBlurNum(3);
        redFog = new PointLight(rayHandler2, 128, new Color(0xff000055), 200, Gdx.graphics.getWidth() / PhysicsSystem.PIXEL_PER_METER, Gdx.graphics.getHeight() / PhysicsSystem.PIXEL_PER_METER);
        yellowFog = new PointLight(rayHandler2, 128, new Color(0xffff0033), 200, 0, 0);
        blueFog = new PointLight(rayHandler2, 128, new Color(0x0000ff33), 200, Gdx.graphics.getWidth() / PhysicsSystem.PIXEL_PER_METER, 0);
        greenFog = new PointLight(rayHandler2, 128, new Color(0x00ff0033), 200, 0, Gdx.graphics.getHeight() / PhysicsSystem.PIXEL_PER_METER);


//        DirectionalLight directionalLight = new DirectionalLight(rayHandler, 512, new Color(0,0.01f,0.01f,0.5f), +250);

        EnemySpawnSystem enemySpawnSystem = new EnemySpawnSystem(1f, 3, world, rayHandler);

        ashley.addSystem(new PhysicsSystem(world, 1));
        ashley.addSystem(enemySpawnSystem);
        ashley.addSystem(new HealthSystem());
        ashley.addSystem(new BulletSystem());

        ScoreManager.init(enemySpawnSystem);

        world.setContactListener(new AshleyB2DContactListener());

//        EntityFactory.createBase(world, new Vector2(Gdx.graphics.getWidth() / PhysicsSystem.PIXEL_PER_METER / 2, Gdx.graphics.getHeight() / PhysicsSystem.PIXEL_PER_METER / 2), 0);
//        EntityFactory.createEnemy(world, new Vector2(10, 50), 45 * MathUtils.degreesToRadians, 500);

//        EntityFactory.createEnemy(world, new Vector2(10, 50), 0 * MathUtils.degreesToRadians, 0, rayHandler);


        Entity player = EntityFactory.createPlayer(world, new Vector2((Gdx.graphics.getWidth()/ 2 ) / PhysicsSystem.PIXEL_PER_METER, (Gdx.graphics.getHeight()/2) / PhysicsSystem.PIXEL_PER_METER), 0, rayHandler);
        physicsComponent = ComponentMappers.physics.get(player);

        engineLight = new ConeLight(rayHandler, 4, new Color(1, 1, 1, 1), 30, 0, 0, 180, 15);
        engineLight.attachToBody(physicsComponent.body, 0, -1.5f, -90);

        debugRenderer = new Box2DDebugRenderer(true, true, false, true, true, true);

        background = Statics.asset.getTexture(Textures.BACKGROUND);
        backgroundBatch = new SpriteBatch();
        initShader();

        overlays.add(new ParticleOverlay(camera));

        for(AbstractOverlay overlay : overlays) {
            overlay.init();
        }

    }

    private void initShader() {
        backgroundShader = Statics.asset.getShader(ShaderPrograms.BACKGROUND);
        backgroundBatch.setShader(backgroundShader);
    }

    float timeInS = 50.0f;
    float deltaSum = Float.MAX_VALUE;
    float deltaSum2 = Float.MAX_VALUE;


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        timeInS += delta;
        backgroundShader.begin();
        backgroundShader.setUniformf("iTime", timeInS);
        backgroundShader.end();

        backgroundBatch.begin();
        backgroundBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundBatch.end();
//
        ScoreManager.addTime(delta);
        ashley.update(delta);
//        debugRenderer.render(world, debugCamera.combined);
        rayHandler2.setCombinedMatrix(debugCamera);
        rayHandler2.updateAndRender();
        rayHandler.setCombinedMatrix(debugCamera);
        rayHandler.updateAndRender();

//        float a = Gdx.graphics.getWidth()/2;
//        float b = 1.0f;
//        float h = 0.0f;
//        float k = a;

//        redFog.setPosition(a * MathUtils.sin(b*(redFog.getX()-h)) + k, redFog.getY());

        deltaSum+=delta;
        deltaSum2+=delta;
        if (Gdx.input.isTouched()) {
            if(deltaSum > 0.2){
                Vector2 angleDiff = new Vector2(3, 0);
                angleDiff.setAngle(physicsComponent.body.getAngle() * MathUtils.radiansToDegrees + 90);
                EntityFactory.createBullet(world, physicsComponent.body.getPosition().add(angleDiff), physicsComponent.body.getAngle(), 50, rayHandler);
                physicsComponent.body.applyForceToCenter(new Vector2(0, -100).rotateRad(physicsComponent.body.getAngle()), true);

                angleDiff.setAngle(physicsComponent.body.getAngle() * MathUtils.radiansToDegrees + 120);
                EntityFactory.createBullet(world, physicsComponent.body.getPosition().add(angleDiff), physicsComponent.body.getAngle()+0.1f, 50, rayHandler);

                angleDiff.setAngle(physicsComponent.body.getAngle() * MathUtils.radiansToDegrees + 70);
                EntityFactory.createBullet(world, physicsComponent.body.getPosition().add(angleDiff), physicsComponent.body.getAngle()-0.1f, 50, rayHandler);

                SoundManager.playShotSound();

                deltaSum = 0;
            }

        }


        boolean engine = false;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            physicsComponent.body.applyForceToCenter(new Vector2(0, 400).rotateRad(physicsComponent.body.getAngle()), true);
            engine = true;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.A)||Gdx.input.isKeyJustPressed(Input.Keys.D)){
            if(deltaSum2 > 2){
                SoundManager.playShipRotate();
                deltaSum2 = 0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//            physicsComponent.body.applyForceToCenter(new Vector2(-400, 0), true);
            physicsComponent.body.applyAngularImpulse(5, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            physicsComponent.body.applyForceToCenter(new Vector2(-400, 0).rotateRad(physicsComponent.body.getAngle()), true);
//            physicsComponent.body.applyAngularImpulse(5, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            physicsComponent.body.applyForceToCenter(new Vector2(0, -400).rotateRad(physicsComponent.body.getAngle()), true);
            engine = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//            physicsComponent.body.applyForceToCenter(new Vector2(400, 0), true);
            physicsComponent.body.applyAngularImpulse(-5, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            physicsComponent.body.applyForceToCenter(new Vector2(400, 0).rotateRad(physicsComponent.body.getAngle()), true);
//            physicsComponent.body.applyAngularImpulse(-5, true);
        }


        if (engine) {
            float currentSpeed = physicsComponent.body.getLinearVelocity().len();
            engineLight.setActive(true);
            engineLight.setConeDegree(45 - currentSpeed);
            engineLight.setDistance(currentSpeed);
            if (engineLight.getConeDegree() < 5) {
                engineLight.setConeDegree(5);
            }
            if (currentSpeed <= lastSpeed) {
                SoundManager.pauseEngine();
            } else {
                SoundManager.startEngine();
            }
            lastSpeed = currentSpeed;
        } else {
            engineLight.setConeDegree(30);
            engineLight.setActive(false);
            SoundManager.stopEngine();
        }

        for(AbstractOverlay overlay : overlays) {
            overlay.render(delta);
        }

        // remove dirty entities
        if (dirtyEntities.size() > 0) {
            for (Entity entity : dirtyEntities) {
                if(ComponentMappers.player.has(entity)) {
                    // Game Over;
                    LDGame.game.setCurrentScreen(new GameOverScreen());
                } else if (ComponentMappers.physics.has(entity)) {
                    for(Light light : ComponentMappers.physics.get(entity).lights) {
                        light.remove();
                    }
                    world.destroyBody(ComponentMappers.physics.get(entity).body);
                }
                ashley.removeEntity(entity);
            }
        }

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
        backgroundShader.begin();
        backgroundShader.setUniformf("iResolution", width, height);
        backgroundShader.end();
    }

    @Override
    public void dispose() {
        rayHandler.dispose();
        backgroundBatch.dispose();
        world.dispose();
        for(AbstractOverlay overlay : overlays) {
            overlay.dispose();
        }

    }
}