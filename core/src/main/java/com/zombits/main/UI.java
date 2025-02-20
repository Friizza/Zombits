package com.zombits.main;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class UI {

    GamePanel gp;
//    SpriteBatch batch;
    Texture logo;
    Texture uiBackground;

    public UI(GamePanel gp) {
        this.gp = gp;
    }

    public void drawUI() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
//        batch.begin();
//
//        batch.end();
    }

    public void dispose() {
        logo.dispose();
        uiBackground.dispose();
    }
}
