package de.dogedev.ld40.assets.enums;

public enum Textures {
    PLAYER("textures/player_outline.png"),
    BULLET("textures/bullet_outline.png"),
    ASTEROID_1("textures/asteroid1.png"),
    ASTEROID_2("textures/asteroid2.png"),
    ENEMY("textures/enemy.png"),
    SHIELD("textures/circle.png"),
    BACKGROUND("textures/background.png");

    public String name;

    Textures(String name) {
        this.name = name;
    }
}
