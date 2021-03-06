package de.dogedev.ld40;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import de.dogedev.ld40.screens.LoadingScreen;

public class LDGame extends Game {
    public static LDGame game;


    @Override
    public void create() {
        game = this;
        setCurrentScreen(new LoadingScreen());
    }

    public void setCurrentScreen(Screen screen) {
        setScreen(screen);
    }
}
