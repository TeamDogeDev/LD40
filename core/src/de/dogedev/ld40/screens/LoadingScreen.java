package de.dogedev.ld40.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.dogedev.ld40.LDGame;
import de.dogedev.ld40.Statics;
import de.dogedev.ld40.misc.SoundManager;

public class LoadingScreen extends ScreenAdapter {

    private ShapeRenderer shapeRenderer;

    public LoadingScreen() {
        init();
        Statics.initCat();
    }

    private void init() {
        shapeRenderer = new ShapeRenderer();
    }

    private void update(float delta) {
        if(Statics.asset.load()) {
            SoundManager.startMusic();
            LDGame.game.setCurrentScreen(new MenuScreen());
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        float progress = Statics.asset.progress();
        float val = 191 * progress;
        Gdx.gl.glClearColor(val / 255f, val / 255f, val / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.25f, 0.25f, 0.25f, 1);
        float width = 300;
        float height = 20;
        float x = (Gdx.graphics.getWidth() - width) / 2;
        float y = (Gdx.graphics.getHeight() - height) / 2;
        shapeRenderer.rect(x, y, width * progress, height);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
