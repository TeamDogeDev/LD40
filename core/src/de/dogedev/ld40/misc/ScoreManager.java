package de.dogedev.ld40.misc;

import de.dogedev.ld40.ashley.systems.EnemySpawnSystem;
import de.dogedev.ld40.ashley.systems.OverlayRenderSystem;

/**
 * Created by meisterfuu.
 */
public class ScoreManager {

    private static int currentKills;
    private static float currentTime;
    private static EnemySpawnSystem enemySpawnSystem;

    public static void init(EnemySpawnSystem enemySpawnSystem){
        ScoreManager.enemySpawnSystem = enemySpawnSystem;
        currentKills = 0;
        currentTime = 0;
    }

    public static void addTime(float delta){
        currentTime += delta;
        // 15 seconds
        if(((int) currentTime) % 3 == 0) {
            // Next Phase
            enemySpawnSystem.decreaseUpdateInterval();
            OverlayRenderSystem.flashText();
        }
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
