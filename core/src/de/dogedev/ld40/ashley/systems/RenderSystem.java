package de.dogedev.ld40.ashley.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.dogedev.ld40.ashley.ComponentMappers;
import de.dogedev.ld40.ashley.components.HiddenComponent;
import de.dogedev.ld40.ashley.components.LookComponent;
import de.dogedev.ld40.ashley.components.PositionComponent;
import de.dogedev.ld40.ashley.components.RenderComponent;

/**
 * Created by Furuha on 28.01.2016.
 */
public class RenderSystem extends EntitySystem implements EntityListener {

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private Array<Entity> sortedEntities;
    private BitmapFont font;
    ImmutableArray<Entity> entities;

    public RenderSystem(OrthographicCamera camera, int priority) {
        super(priority);
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.sortedEntities = new Array<>();
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

            drawRotated(renderComponent.region, positionComponent.x - renderComponent.region.getRegionWidth() / 2, positionComponent.y - renderComponent.region.getRegionHeight() / 2, positionComponent.rotation-90);
        }
    }

    private void draw(TextureRegion region, float x, float y) {
        batch.draw(region.getTexture(), x, y, region.getRegionWidth() / 2, region.getRegionHeight() / 2, region.getRegionWidth(), region.getRegionHeight(), 1, 1, 0, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), false, false);
    }

    private void drawRotated(TextureRegion region, float x, float y, float angle) {
        batch.draw(region.getTexture(), x, y, region.getRegionWidth() / 2, region.getRegionHeight() / 2, region.getRegionWidth(), region.getRegionHeight(), 1, 1, angle, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), false, false);
    }

}