package de.dogedev.ld40.assets.enums;

public enum Particles {
    HOT_EXPLOSION_PARTICLE("particles/hotExplosion.p", "particles"),
    COLD_EXPLOSION_PARTICLE("particles/coldExplosion.p", "particles");

    public String effectFile;
    public String imageDir;

    Particles(String effectFile, String imageDir) {
        this.effectFile = effectFile;
        this.imageDir = imageDir;
    }
}
