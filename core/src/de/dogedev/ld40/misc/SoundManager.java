package de.dogedev.ld40.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Created by meisterfuu.
 */
public class SoundManager {

    private static Music engineStart;
    private static Music engineStart2;
    private static Music engineRunning;
    private static Music engineSlowdown;

    public static boolean engineOn = false;

    public static void init(){
        engineSlowdown = Gdx.audio.newMusic(Gdx.files.internal("sounds/241262__lewis100011__sci-fi-engine.ogg"));
        engineRunning = Gdx.audio.newMusic(Gdx.files.internal("sounds/101278__tomlija__air-pump-drone-and-hum.ogg"));
        engineStart = Gdx.audio.newMusic(Gdx.files.internal("sounds/98630__robinhood76__01820-futuristic-space-foley.ogg"));
        engineStart2 = Gdx.audio.newMusic(Gdx.files.internal("sounds/98630__robinhood76__01820-futuristic-space-foley_end.ogg"));
        engineSlowdown.setVolume(0.17f);
    }

    public static void startEngine(){
        if(!engineOn){
//            engineRunning.stop();
//            engineSlowdown.stop();
            engineStart2.stop();
            engineStart.play();
            engineOn = true;
        }
    }

    public static void pauseEngine(){
            engineRunning.pause();
    }

    public static void stopEngine(){
        if(engineOn) {
            engineStart.stop();
            if(engineStart.getPosition() > 5){
                engineStart2.setVolume(1);
            } else {
                engineStart2.setVolume(0.17f);
            }
            engineStart2.play();
//            engineStart.stop();
//            engineSlowdown.play();
            engineOn = false;
        }
    }


}
