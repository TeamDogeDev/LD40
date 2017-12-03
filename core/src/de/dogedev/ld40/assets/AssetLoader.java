package de.dogedev.ld40.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import de.dogedev.ld40.assets.enums.ShaderPrograms;
import de.dogedev.ld40.assets.enums.Textures;

public class AssetLoader implements Disposable {

    private AssetManager manager = new AssetManager();
    private TextureAtlas atlas;

    public AssetLoader() {
         loadTextures();
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
}
