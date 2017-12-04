package de.dogedev.ld40.misc;

/**
 * Created by meisterfuu.
 */
public class ScoreManager {

    private static int currentKills;
    private static float currentTime;

    public static void init(){
        currentKills = 0;
        currentTime = 0;
    }

    public static void addTime(float delta){
        currentTime += delta;
    }

    public static void addKill(){
        currentKills += 1;
    }

    public static int getCurrentKills() {
        return currentKills;
    }

    public static int getCurrentTime() {
        return (int) currentTime;
    }

}
