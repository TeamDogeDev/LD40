package de.dogedev.ld40.ashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import de.dogedev.ld40.Statics;
import de.dogedev.ld40.ashley.ComponentMappers;
import de.dogedev.ld40.ashley.components.PlayerComponent;
import de.dogedev.ld40.ashley.components.PositionComponent;
import de.dogedev.ld40.assets.enums.BitmapFonts;
import de.dogedev.ld40.assets.enums.ShaderPrograms;
import de.dogedev.ld40.assets.enums.Textures;
import de.dogedev.ld40.misc.ScoreManager;

public class OverlayRenderSystem extends EntitySystem {

    private static boolean flashTextVisible = false;
    int[] healthLut = {0x00ff0055,
            0x05ff0055,
            0x0bfe0055,
            0x10fd0055,
            0x16fc0055,
            0x1bfb0055,
            0x21fa0055,
            0x26f90055,
            0x2bf90055,
            0x31f80055,
            0x36f70055,
            0x3cf60055,
            0x41f50055,
            0x47f40055,
            0x4cf30055,
            0x51f20055,
            0x57f10055,
            0x5cf00055,
            0x62ef0055,
            0x67ef0055,
            0x6dee0055,
            0x72ed0055,
            0x78ec0055,
            0x7deb0055,
            0x82ea0055,
            0x88ea0055,
            0x8dea0055,
            0x91eb0055,
            0x96ec0055,
            0x9bed0055,
            0xa0ee0055,
            0xa5ef0055,
            0xaaf00055,
            0xaff10055,
            0xb4f20055,
            0xb9f30055,
            0xbef40055,
            0xc3f40055,
            0xc7f50055,
            0xccf60055,
            0xd1f70055,
            0xd6f80055,
            0xdbf90055,
            0xe0fa0055,
            0xe5fb0055,
            0xeafc0055,
            0xeffd0055,
            0xf4fe0055,
            0xf8fe0055,
            0xfdff0055,
            0xfffd0055,
            0xfff80055,
            0xfef30055,
            0xfdee0055,
            0xfce80055,
            0xfbe30055,
            0xfbde0055,
            0xfad90055,
            0xf9d40055,
            0xf8cf0055,
            0xf8c90055,
            0xf7c40055,
            0xf6bf0055,
            0xf5ba0055,
            0xf4b50055,
            0xf4af0055,
            0xf3aa0055,
            0xf2a50055,
            0xf1a00055,
            0xf19b0055,
            0xf0960055,
            0xef900055,
            0xee8b0055,
            0xed860055,
            0xed810055,
            0xed7c0055,
            0xee760055,
            0xef710055,
            0xef6c0055,
            0xf0670055,
            0xf1620055,
            0xf25d0055,
            0xf3580055,
            0xf3520055,
            0xf44d0055,
            0xf5480055,
            0xf6430055,
            0xf63e0055,
            0xf7390055,
            0xf8340055,
            0xf92e0055,
            0xfa290055,
            0xfa240055,
            0xfb1f0055,
            0xfc1a0055,
            0xfd150055,
            0xfd0f0055,
            0xfe0a0055,
            0xff050055,
            0xff000000};


    private static Array<Color> flashColors = new Array<>();
    private final ShaderProgram shader;
    private ImmutableArray<Entity> entites;
    private Batch batch;
    private float deltaSum = 0.0f;
    private Texture shieldTexture;
    private Texture flashTexture;
    private BitmapFont font;

    private static Color flashColor = Color.RED;

    public OverlayRenderSystem(int priority) {
        super(priority);
        shader = Statics.asset.getShader(ShaderPrograms.GLOW);
        shieldTexture = Statics.asset.getTexture(Textures.SHIELD);
        flashTexture = Statics.asset.getTexture(Textures.FLASHTEXT);
        font = Statics.asset.getFont(BitmapFonts.GAMEFONT);
        batch = new SpriteBatch();
        batch.setShader(shader);

        flashColors.add(Color.RED);
        flashColors.add(Color.YELLOW);
        flashColors.add(Color.CYAN);
        flashColors.add(Color.BLUE);
        flashColors.add(Color.PURPLE);
        flashColors.add(Color.MAGENTA);
    }


    @Override
    public void addedToEngine(Engine engine) {
        entites = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    void updateShader(float delta) {
        deltaSum += delta;

        float a = 0.15f;
        float b = 3;
        float h = 0;
        float k = a;

        float value = (a * MathUtils.sin(b * (deltaSum - h))) + k;
        shader.begin();
        shader.setUniformf("iIntensity", value);
        shader.end();
    }

    /**
     * @param health \in [0, 1]
     * @return
     */
    private Color healthToColor(float health) {
        int lookup = (int) MathUtils.clamp((1 - health) * 100, 0, healthLut.length - 1);
        Color color = new Color(healthLut[lookup]);
        color.a = 0.33f;
        return color;
    }

    private static Color randomFlashColor() {
        return flashColors.get(MathUtils.random(0, flashColors.size-1));
    }
    private static float flashCounter = 0.0f;
    public static void flashText() {
        if(!flashTextVisible) {
            Color newColor  = randomFlashColor();
            while(flashColor == newColor) {
                newColor = randomFlashColor();
            }
            flashColor = newColor;
            flashTextVisible = true;
        }
    }


    @Override
    public void update(float deltaTime) {
        updateShader(deltaTime);
        PositionComponent pc;
        batch.begin();
        batch.setShader(shader);
        for (Entity e : entites) {
            pc = ComponentMappers.position.get(e);
            if (ComponentMappers.health.has(e)) {
                float playerHealth = ComponentMappers.health.get(e).health;
                batch.setColor(healthToColor(playerHealth));
            } else {
                batch.setColor(Color.CYAN);
            }
            batch.draw(shieldTexture, pc.x - shieldTexture.getWidth() / 2, pc.y - Statics.asset.getTexture(Textures.SHIELD).getHeight() / 2);
        }
        if(flashTextVisible) {
            batch.setColor(flashColor);
            batch.draw(flashTexture, (Gdx.graphics.getWidth() - flashTexture.getWidth()) / 2, (Gdx.graphics.getHeight() - flashTexture.getHeight()) / 2);
            flashCounter+= deltaTime;
        }

        if(flashCounter >= 2.0f) {
            flashTextVisible = false;
            flashCounter = 0.0f;
        }

        batch.setShader(null);
//        font.setColor(new Color(0xff000055));
        font.draw(batch, "Score " + ScoreManager.getCurrentTime(), 10, Gdx.graphics.getHeight() - 10);
        batch.end();
    }
}
