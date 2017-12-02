package de.dogedev.ld40;

import com.badlogic.ashley.core.PooledEngine;
import de.dogedev.ld40.assets.AssetLoader;

public class Statics {

    public static PooledEngine ashley;
    public static AssetLoader asset;


    public static void initCat() {
        asset = new AssetLoader();
        ashley = new PooledEngine();
    }
}
