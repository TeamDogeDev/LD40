package de.dogedev.ld40.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import de.dogedev.ld40.LDGame;
import de.dogedev.ld40.Statics;
import de.dogedev.ld40.assets.enums.BitmapFonts;
import de.dogedev.ld40.assets.enums.ShaderPrograms;
import de.dogedev.ld40.assets.enums.Textures;

import java.util.Map;

public class MenuScreen extends ScreenAdapter {

    private Batch batch;
    private Texture background;
    private Texture title;
    private ShaderProgram backgroundShader;
    private ShaderProgram titleShader;
    private BitmapFont font;

    private Array<String> elements = new Array<>();
    private String selectedMenuPoint = "Start";

    public MenuScreen() {
        super();
        init();
        elements.add("Start");
        elements.add("Exit");
    }

    private void init() {
        font = Statics.asset.getFont(BitmapFonts.GAMEFONT);
        background = Statics.asset.getTexture(Textures.BACKGROUND);
        title = Statics.asset.getTexture(Textures.TITLE);

        batch = new SpriteBatch();
        backgroundShader = Statics.asset.getShader(ShaderPrograms.MENU);
        titleShader = Statics.asset.getShader(ShaderPrograms.GLOW);
        batch.setShader(backgroundShader);
    }

    private float timeInS;

    private void updateShader(float delta) {
        timeInS += delta;
        backgroundShader.begin();
        backgroundShader.setUniformf("iTime", timeInS);
        backgroundShader.end();

        float a = 0.35f;
        float b = 5;
        float h = 0;
        float k = a;

        float value = (a * MathUtils.sin(b * (timeInS - h))) + k;
        titleShader.begin();
        titleShader.setUniformf("iIntensity", value);
        titleShader.end();
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
        batch.setShader(titleShader);

        batch.setColor(new Color(0xffff0055));
        batch.draw(title, (Gdx.graphics.getWidth()- title.getWidth())/2, Gdx.graphics.getHeight() - title.getHeight()- 50);
        batch.setShader(null);
        int idx = 0;
        for(String s : elements) {
            float additionalOffset = 0.0f;
            if(s.equalsIgnoreCase(selectedMenuPoint)) {
                additionalOffset = MathUtils.sin(timeInS*2)*10;
            }
            font.draw(batch, s, (Gdx.graphics.getWidth() / 2), (Gdx.graphics.getHeight()/2) - (idx++*(font.getLineHeight()*2) + additionalOffset),
                    2, Align.center, false);
        }
        batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            // previous
            if(selectedMenuPoint.equalsIgnoreCase("Start")) {
                selectedMenuPoint = "Exit";
            } else {
                selectedMenuPoint = "Start";
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if(selectedMenuPoint.equalsIgnoreCase("Exit")) {
                Gdx.app.exit();
            } else {
                LDGame.game.setCurrentScreen(new GameScreen());
            }
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
