package de.dogedev.ld40;

import com.badlogic.ashley.core.PooledEngine;
import de.dogedev.ld40.assets.AssetLoader;
import de.dogedev.ld40.assets.ParticlePool;

public class Statics {

    public static PooledEngine ashley;
    public static AssetLoader asset;
    public static ParticlePool particle;


    public static void initCat() {
        asset = new AssetLoader();
        ashley = new PooledEngine();
        particle = new ParticlePool();
    }
}
