package de.dogedev.ld40.overlays;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import de.dogedev.ld40.Statics;
import de.dogedev.ld40.assets.enums.ShaderPrograms;

public class ParticleOverlay extends AbstractOverlay {

    private ShaderProgram shader;
    float intensity = 0.0f;

    public ParticleOverlay() {
    }

    @Override
    public void init() {
        shader = Statics.asset.getShader(ShaderPrograms.GLOW);
        batch.setShader(shader);
    }

    @Override
    public void update(float delta) {
        float a = 0.15f;
        float b = 3;
        float h = 0;
        float k = a;
        intensity += delta;

        float value = (a * MathUtils.sin(b * (intensity - h))) + k;
        shader.begin();
        shader.setUniformf("iIntensity", value);
        shader.end();
    }

    @Override
    public void render(float deltaTime) {
        update();
        batch.begin();
        for(ParticleEffectPool.PooledEffect effect : Statics.particle.getEffects()) {
            effect.draw(batch, deltaTime);
            if(effect.isComplete()) {
                Statics.particle.removeEffect(effect, true);
                effect.free();
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        Statics.particle.dispose();
    }
}
