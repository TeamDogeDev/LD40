package de.dogedev.ld40.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import de.dogedev.ld40.Statics;
import de.dogedev.ld40.ashley.components.DirtyComponent;
import de.dogedev.ld40.ashley.components.PhysicsComponent;
import de.dogedev.ld40.ashley.components.PositionComponent;
import de.dogedev.ld40.ashley.components.RenderComponent;
import de.dogedev.ld40.ashley.systems.DebugUISystem;
import de.dogedev.ld40.ashley.systems.MovementSystem;
import de.dogedev.ld40.ashley.systems.PhysicsSystem;
import de.dogedev.ld40.ashley.systems.RenderSystem;
import de.dogedev.ld40.assets.enums.Textures;
import javafx.geometry.Pos;

import static de.dogedev.ld40.Statics.ashley;
import static de.dogedev.ld40.Statics.asset;

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

        world = new World(new Vector2(0, -9.8f), false);
        ashley.addSystem(new PhysicsSystem(world, 1));

        for (int j = 0; j < 2; j++) {

        for (int i = 0; i < 10; i++) {
            BodyDef entityBody = new BodyDef();
            entityBody.type = BodyDef.BodyType.DynamicBody;
            entityBody.position.set(i*20, 50 + (j*20) + MathUtils.random(0, 10));
            entityBody.angle = MathUtils.PI / 3;

            PolygonShape entityShape = new PolygonShape();
            entityShape.setAsBox(12.8f/2, 12.8f/2);

            FixtureDef entityFixture = new FixtureDef();
            entityFixture.shape = entityShape;
            entityFixture.density = 1f;

            PhysicsComponent physicsComponent = ashley.createComponent(PhysicsComponent.class);
            physicsComponent.body = world.createBody(entityBody);
            physicsComponent.body.createFixture(entityFixture);
            entityShape.dispose();

            PositionComponent positionComponent = ashley.createComponent(PositionComponent.class);
            RenderComponent renderComponent = ashley.createComponent(RenderComponent.class);
            renderComponent.region = asset.getTextureRegion(Textures.ENEMY);

            Entity entity = ashley.createEntity();
            entity.add(positionComponent);
            entity.add(renderComponent);
            entity.add(physicsComponent);

            ashley.addEntity(entity);
        }
        }

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
