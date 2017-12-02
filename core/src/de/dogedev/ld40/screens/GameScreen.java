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
import de.dogedev.ld40.Statics;
import de.dogedev.ld40.ashley.components.DirtyComponent;
import de.dogedev.ld40.ashley.systems.DebugUISystem;
import de.dogedev.ld40.ashley.systems.MovementSystem;
import de.dogedev.ld40.ashley.systems.RenderSystem;
import de.dogedev.ld40.assets.enums.Textures;

import static de.dogedev.ld40.Statics.ashley;

public class GameScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture enemy, player, base;
    private OrthographicCamera camera;
    private RenderSystem renderSystem;
    private ImmutableArray<Entity> dirtyEntities;

    public GameScreen() {
        init();
    }

    public void init () {
        ashley.removeAllEntities();

        camera = new OrthographicCamera();
        camera.zoom = 1f;
        camera.setToOrtho(false, 1280, 720);

        ashley.addSystem(new MovementSystem(2));
        renderSystem = new RenderSystem(camera, 5);
        ashley.addSystem(renderSystem);
        ashley.addSystem(new DebugUISystem(camera, 6));

        dirtyEntities = ashley.getEntitiesFor(Family.all(DirtyComponent.class).get());




        batch = new SpriteBatch();
        enemy = Statics.asset.getTexture(Textures.ENEMY);
        player = Statics.asset.getTexture(Textures.PLAYER);
        base = Statics.asset.getTexture(Textures.BASE);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ashley.update(delta);


        batch.begin();
        batch.draw(base, 0, 0, base.getWidth()>>1, base.getHeight()>>1);
        batch.draw(player, 300, 0, player.getWidth()>>1, player.getHeight()>>1);
        batch.draw(enemy, 512, 0, enemy.getWidth()>>1, enemy.getHeight()>>1);
        batch.end();



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
    public void dispose () {
        batch.dispose();
    }
}
