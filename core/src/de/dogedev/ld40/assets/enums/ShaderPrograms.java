package de.dogedev.ld40.assets.enums;

public enum ShaderPrograms {
    BACKGROUND("shader/background.vert", "shader/background.frag"),
    EDGE("shader/pass.vert", "shader/glow.frag");

    public String vertexShaderFile;
    public String fragmentShaderFile;

    ShaderPrograms(String vertexShaderFile, String fragmentShaderFile) {
        this.vertexShaderFile = vertexShaderFile;
        this.fragmentShaderFile = fragmentShaderFile;
    }

}