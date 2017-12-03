package de.dogedev.ld40.ashley.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import de.dogedev.ld40.Statics;
import de.dogedev.ld40.ashley.ComponentMappers;
import de.dogedev.ld40.ashley.components.HiddenComponent;
import de.dogedev.ld40.ashley.components.PositionComponent;
import de.dogedev.ld40.ashley.components.RenderComponent;
import de.dogedev.ld40.assets.enums.ShaderPrograms;

/**
 * Created by Furuha on 28.01.2016.
 */
public class RenderSystem extends EntitySystem implements EntityListener {

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private Array<Entity> sortedEntities;
    private BitmapFont font;
    ImmutableArray<Entity> entities;
    ShaderProgram shader;

    public RenderSystem(OrthographicCamera camera, int priority) {
        super(priority);
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.sortedEntities = new Array<>();
        shader = Statics.asset.getShader(ShaderPrograms.EDGE);
        batch.setShader(shader);



    }

    float intensity = 0.0f;

    private void updateShader(float delta) {
        float a = 0.15f;
        float b = 10;
        float h = 0;
        float k = a;
        intensity += delta;

        float value = (a * MathUtils.sin(b*(intensity-h)))+k;
        shader.begin();
        shader.setUniformf("iIntensity", value);
        shader.end();
    }

    @Override
    public void addedToEngine(Engine pengine) {
        entities = pengine.getEntitiesFor(Family.one(PositionComponent.class).one(RenderComponent.class).exclude(HiddenComponent.class).get());

        PooledEngine engine = (PooledEngine) pengine;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(this);
    }

    @Override
    public void entityAdded(Entity entity) {
        sortedEntities.add(entity);
    }

    @Override
    public void entityRemoved(Entity entity) {
        sortedEntities.removeValue(entity, false);
    }


    @Override
    public void update(float deltaTime) {
        updateShader(deltaTime);
        try {
            sortedEntities.sort();
        } catch (Exception e) {
            System.err.println("Render: Sort contract violation ~ Carry on");
        }

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            drawEntity(e, deltaTime);
        }
        batch.end();

    }

    private void drawEntity(Entity e, float deltaTime) {
        RenderComponent renderComponent = ComponentMappers.render.get(e);

        if (ComponentMappers.position.has(e)) {
            PositionComponent positionComponent = ComponentMappers.position.get(e);
            drawRotated(renderComponent.region, positionComponent.x - renderComponent.region.getRegionWidth() / 2, positionComponent.y - renderComponent.region.getRegionHeight() / 2, positionComponent.rotation);
        }
    }

    private void draw(TextureRegion region, float x, float y) {
        batch.draw(region.getTexture(), x, y, region.getRegionWidth() / 2, region.getRegionHeight() / 2, region.getRegionWidth(), region.getRegionHeight(), 1, 1, 0, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), false, false);
    }

    private void drawRotated(TextureRegion region, float x, float y, float angle) {
        batch.draw(region.getTexture(), x, y, region.getRegionWidth() / 2, region.getRegionHeight() / 2, region.getRegionWidth(), region.getRegionHeight(), 1, 1, angle, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), false, false);
    }

}