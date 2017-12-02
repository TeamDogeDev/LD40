package de.dogedev.ld40.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.dogedev.ld40.Statics;
import de.dogedev.ld40.assets.enums.Textures;

public class GameScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture enemy, player, base;

    public GameScreen() {
        init();
    }

    public void init () {
        batch = new SpriteBatch();
        enemy = Statics.asset.getTexture(Textures.ENEMY);
        player = Statics.asset.getTexture(Textures.PLAYER);
        base = Statics.asset.getTexture(Textures.BASE);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, .54f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(base, 0, 0, base.getWidth()>>1, base.getHeight()>>1);
        batch.draw(player, 300, 0, player.getWidth()>>1, player.getHeight()>>1);
        batch.draw(enemy, 512, 0, enemy.getWidth()>>1, enemy.getHeight()>>1);
        batch.end();
    }


    @Override
    public void dispose () {
        batch.dispose();
    }
}
