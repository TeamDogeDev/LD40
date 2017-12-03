package de.dogedev.ld40.overlays;

import de.dogedev.ld40.Statics;
import de.dogedev.ld40.assets.enums.Textures;

public class HealthOverlay extends AbstractOverlay {

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(Statics.asset.getTexture(Textures.BASE), 0, 0);
        batch.end();
    }
}
