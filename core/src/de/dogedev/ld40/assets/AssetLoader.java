package de.dogedev.ld40.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import de.dogedev.ld40.assets.enums.BitmapFonts;
import de.dogedev.ld40.assets.enums.Particles;
import de.dogedev.ld40.assets.enums.ShaderPrograms;
import de.dogedev.ld40.assets.enums.Textures;

public class AssetLoader implements Disposable {

    private AssetManager manager = new AssetManager();
    private TextureAtlas atlas;

    public AssetLoader() {
         loadTextures();
         loadFonts();
         loadParticles();
    }

    private void loadParticles() {
        for(Particles particle : Particles.values()) {
            ParticleEffectLoader.ParticleEffectParameter params = new ParticleEffectLoader.ParticleEffectParameter();
            params.imagesDir = Gdx.files.internal(particle.imageDir);
            manager.load(particle.effectFile, ParticleEffect.class, params);
        }
    }

    private void loadFonts() {
        for(BitmapFonts font : BitmapFonts.values()) {
            manager.load(font.name, BitmapFont.class);
        }
    }

    private void loadTextures() {
        // manager.load("....atlas", TextureAtlas.class);
        for(Textures texture : Textures.values()) {
            manager.load(texture.name, Texture.class);
        }
    }

    public ShaderProgram getShader(ShaderPrograms shaderProgram) {
        ShaderProgram.pedantic = false;
        ShaderProgram shader = new ShaderProgram(
                Gdx.files.internal(shaderProgram.vertexShaderFile),
                Gdx.files.internal(shaderProgram.fragmentShaderFile)
        );
        System.out.println(shader.isCompiled() ? shaderProgram.name() + " compiled." : shader.getLog());
        return shader;
    }


    public TextureRegion getTextureRegion(Textures texture) {
        return new TextureRegion(manager.get(texture.name, Texture.class));
    }

    public BitmapFont getFont(BitmapFonts font) {
        return manager.get(font.name, BitmapFont.class);
    }

    public Texture getTexture(Textures texture) {
        return manager.get(texture.name, Texture.class);
    }

    public boolean load() {
        return manager.update();
    }

    public float progress() {
        return manager.getProgress();
    }

    @Override
    public void dispose() {
        manager.dispose();
    }

    public ParticleEffect getParticleEffect(Particles effect) {
        return manager.get(effect.effectFile, ParticleEffect.class);
    }
}
