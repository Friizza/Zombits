package com.zombits.main;

import com.badlogic.gdx.graphics.Texture;

public class Crosshair {

    GamePanel gp;

    public Texture image;

    public int x = 30;
    public int y = 30;

    public Crosshair(GamePanel gp) {
        this.gp = gp;
    }

    public void dispose() {
        image.dispose();
    }
}
