package com.zombits.main;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UI {

    GamePanel gp;
    SpriteBatch batch;
    Texture logo;

    public UI(GamePanel gp, SpriteBatch batch) {
        this.gp = gp;
    }

    public void drawMenu() {
    }

    public void dispose() {
        logo.dispose();
    }
}
