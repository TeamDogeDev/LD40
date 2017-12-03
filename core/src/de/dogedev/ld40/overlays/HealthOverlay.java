package de.dogedev.ld40.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import de.dogedev.ld40.Statics;
import de.dogedev.ld40.assets.enums.ShaderPrograms;
import de.dogedev.ld40.assets.enums.Textures;

public class HealthOverlay extends AbstractOverlay {
    private ShaderProgram shader;
    private float intensity;

    @Override
    public void init() {
        shader = Statics.asset.getShader(ShaderPrograms.EDGE);
    }

    @Override
    public void update(float delta) {
        intensity += delta;

        float a = 0.15f;
        float b = 3;
        float h = 0;
        float k = a;

        float value = (a * MathUtils.sin(b*(intensity-h)))+k;
        shader.begin();
        shader.setUniformf("iIntensity", value);
        shader.end();
    }

    @Override
    public void render() {
        batch.setShader(shader);
        batch.begin();
        batch.draw(Statics.asset.getTexture(Textures.SHIELD), Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        batch.end();
//        batch.setShader(null);
//        batch.begin();
//        batch.draw(Statics.asset.getTexture(Textures.SHIELD), Gdx.graphics.getWidth()/2 - 150, Gdx.graphics.getHeight()/2);
//        batch.end();
    }
}
