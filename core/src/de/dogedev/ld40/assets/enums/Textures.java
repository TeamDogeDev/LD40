package de.dogedev.ld40.assets.enums;

public enum Textures {
    BASE("base.png"),
    PLAYER("player_outline.png"),
    BULLET("bullet.png"),
    ASTEROID_1("asteroid1.png"),
    ASTEROID_2("asteroid2.png"),
    ENEMY("enemy.png"),
    SHIELD("circle.png"),
    BACKGROUND("background.png");

    public String name;

    Textures(String name) {
        this.name = name;
    }
}
