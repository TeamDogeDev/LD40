package de.dogedev.ld40.misc;

import de.dogedev.ld40.ashley.systems.EnemySpawnSystem;
import de.dogedev.ld40.ashley.systems.OverlayRenderSystem;

/**
 * Created by meisterfuu.
 */
public class ScoreManager {

    private static int currentKills;
    private static float currentTime;
    private static float deltaTime;
    private static EnemySpawnSystem enemySpawnSystem;

    public static void init(EnemySpawnSystem enemySpawnSystem){
        ScoreManager.enemySpawnSystem = enemySpawnSystem;
        currentKills = 0;
        currentTime = 0;
    }

    public static void addDeltaTime(){
        deltaTime += 1;
    }

    public static void addTime(float delta){
        currentTime += delta;
        deltaTime += delta;
        // 15 seconds
        if(deltaTime > 10) {
            // Next Phase
            enemySpawnSystem.decreaseUpdateInterval();
            OverlayRenderSystem.flashText();
            deltaTime = 0;
        }
    }

    public static void addKill(){
        currentKills += 0.1f;
        addDeltaTime();
    }

    public static int getCurrentKills() {
        return currentKills;
    }

    public static int getCurrentTime() {
        return (int) currentTime;
    }

}
