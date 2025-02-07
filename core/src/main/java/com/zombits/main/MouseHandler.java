package com.zombits.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class MouseHandler extends InputAdapter {

    GamePanel gp;

    public MouseHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // Convert screen coordinates to world coordinates relative to camera position
        float worldX = gp.camera.position.x - gp.screenWidth/2 +
            screenX * (float)gp.screenWidth / Gdx.graphics.getWidth();
        float worldY = gp.camera.position.y - gp.screenHeight/2 +
            (Gdx.graphics.getHeight() - screenY) * (float)gp.screenHeight / Gdx.graphics.getHeight();

        // Update crosshair position
        gp.crosshair.x = (int)worldX;
        gp.crosshair.y = (int)worldY;

        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT && gp.gameState == gp.playState) {
            // Calculate mouse world position
            float mouseWorldX = gp.camera.position.x - gp.screenWidth/2 +
                screenX * (float)gp.screenWidth / Gdx.graphics.getWidth();
            float mouseWorldY = gp.camera.position.y - gp.screenHeight/2 +
                (Gdx.graphics.getHeight() - screenY) * (float)gp.screenHeight / Gdx.graphics.getHeight();

            // Create and add bullet
            gp.createBullet(gp.player.worldX, gp.player.worldY, mouseWorldX, mouseWorldY);
        }
        return true;
    }
}
