package de.dogedev.ld40.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import de.dogedev.ld40.LDGame;
import de.dogedev.ld40.Statics;
import de.dogedev.ld40.assets.enums.BitmapFonts;
import de.dogedev.ld40.assets.enums.ShaderPrograms;
import de.dogedev.ld40.assets.enums.Textures;
import de.dogedev.ld40.misc.ScoreManager;

public class GameOverScreen extends ScreenAdapter {

    private Batch batch;
    private ShaderProgram backgroundShader;
    private Texture background;
    private BitmapFont font;

    public GameOverScreen() {
        super();
        font = Statics.asset.getFont(BitmapFonts.GAMEFONT);
        background = Statics.asset.getTexture(Textures.BACKGROUND);
        batch = new SpriteBatch();
        backgroundShader = Statics.asset.getShader(ShaderPrograms.MENU);
        batch.setShader(backgroundShader);
    }

    private float timeInS;

    private void updateShader(float delta) {
        timeInS += delta;
        backgroundShader.begin();
        backgroundShader.setUniformf("iTime", timeInS);
        backgroundShader.end();
    }
    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateShader(delta);
        batch.begin();
        batch.setShader(backgroundShader);
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setShader(null);
        font.draw(batch, "Final Score: " + ScoreManager.getCurrentTime(), Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2+100, 2, Align.center, false);
        font.draw(batch, "Retry (Enter)", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 2, Align.center, false);
        font.draw(batch, "Exit (Escape)", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2-font.getLineHeight(), 2, Align.center, false);
        batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            LDGame.game.setCurrentScreen(new GameScreen());
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }


    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }
}
