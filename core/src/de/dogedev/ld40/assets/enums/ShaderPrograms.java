package de.dogedev.ld40.assets.enums;

public enum ShaderPrograms {
    MENU("shader/background.vert", "shader/menu.frag"),
    BACKGROUND("shader/background.vert", "shader/background.frag"),
    GLOW("shader/pass.vert", "shader/glowColor.frag");

    public String vertexShaderFile;
    public String fragmentShaderFile;

    ShaderPrograms(String vertexShaderFile, String fragmentShaderFile) {
        this.vertexShaderFile = vertexShaderFile;
        this.fragmentShaderFile = fragmentShaderFile;
    }

}
