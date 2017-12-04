package de.dogedev.ld40.ashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import de.dogedev.ld40.Statics;
import de.dogedev.ld40.ashley.ComponentMappers;
import de.dogedev.ld40.ashley.components.PlayerComponent;
import de.dogedev.ld40.ashley.components.PositionComponent;
import de.dogedev.ld40.assets.enums.BitmapFonts;
import de.dogedev.ld40.assets.enums.ShaderPrograms;
import de.dogedev.ld40.assets.enums.Textures;

public class OverlayRenderSystem extends EntitySystem {

    private final ShaderProgram shader;
    private ImmutableArray<Entity> entites;
    private Batch batch;
    private float intensity = 0.0f;
    private Texture shieldTexture;
    private BitmapFont font;

    public OverlayRenderSystem(int priority) {
        super(priority);
        shader = Statics.asset.getShader(ShaderPrograms.GLOW);
        shieldTexture = Statics.asset.getTexture(Textures.SHIELD);
        font = Statics.asset.getFont(BitmapFonts.GAMEFONT);
        batch = new SpriteBatch();
        batch.setShader(shader);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entites = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    void updateShader(float delta) {
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
    public void update(float deltaTime) {
        updateShader(deltaTime);
        PositionComponent pc;
        batch.begin();
        batch.setShader(shader);
        for(Entity e : entites) {
            pc = ComponentMappers.position.get(e);
            batch.draw(shieldTexture, pc.x-shieldTexture.getWidth()/2, pc.y-Statics.asset.getTexture(Textures.SHIELD).getHeight()/2);
        }
        batch.setShader(null);
//        font.setColor(new Color(0xff000055));
        font.draw(batch, "Points: N/A", 10, Gdx.graphics.getHeight()-10);
        batch.end();
    }
}
