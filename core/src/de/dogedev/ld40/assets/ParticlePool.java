package de.dogedev.ld40.assets;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import de.dogedev.ld40.Statics;
import de.dogedev.ld40.assets.enums.Particles;

public class ParticlePool implements Disposable{

    private Array<ParticleEffectPool.PooledEffect> effects = new Array<>();

    public void removeEffect(ParticleEffectPool.PooledEffect effect, boolean b) {
        effects.removeValue(effect, b);
    }

    public Array<ParticleEffectPool.PooledEffect> getEffects() {
        return effects;
    }

    @Override
    public void dispose() {
        for(ParticleType type : ParticleType.values()) {
            type.dispose();
        }
    }

    public enum ParticleType implements Disposable {

        COLD_EXPLOSION(new GameEffect(Statics.asset.getParticleEffect(Particles.HOT_EXPLOSION_PARTICLE), 25, 100)),
        HOT_EXPLOSION(new GameEffect(Statics.asset.getParticleEffect(Particles.COLD_EXPLOSION_PARTICLE), 25, 100));

        GameEffect gameEffect;

        ParticleType(GameEffect gameEffect) {
            this.gameEffect = gameEffect;
        }

        @Override
        public void dispose() {
            gameEffect.dispose();
        }
    }

    public ParticlePool() {
    }

    public int getPoolPeek(ParticleType type) {
        return type.gameEffect.pool.peak;
    }

    public int getPoolMax(ParticleType type) {
        return type.gameEffect.pool.max;
    }

    public void createParticleAt(ParticleType particleType, float x, float y) {
        ParticleEffectPool.PooledEffect effect = particleType.gameEffect.pool.obtain();
        effect.reset();
        effect.setPosition(x, y);
        effects.add(effect);
        effect.start();
    }

    private static class GameEffect implements Disposable {
        ParticleEffect prototype;
        ParticleEffectPool pool;

        public GameEffect(ParticleEffect prototype, int initialCapacity, int maxCapacity) {
            this.prototype = prototype;
            pool = new ParticleEffectPool(prototype, initialCapacity, maxCapacity);
        }

        @Override
        public void dispose() {

        }
    }
}
