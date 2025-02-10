package com.zombits.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class MouseHandler extends InputAdapter {

    GamePanel gp;

    public boolean isShooting = false;

    public float mouseX, mouseY;

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

    public void updateMouse() {
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT && gp.gameState == gp.playState) {
            isShooting = true;
//            gp.gameSound.playSE(gp.gameSound.shoot);

            // Calculate mouse world position
            float mouseWorldX = gp.camera.position.x - gp.screenWidth/2 +
                screenX * (float)gp.screenWidth / Gdx.graphics.getWidth();
            float mouseWorldY = gp.camera.position.y - gp.screenHeight/2 +
                (Gdx.graphics.getHeight() - screenY) * (float)gp.screenHeight / Gdx.graphics.getHeight();

            mouseX = mouseWorldX;
            mouseY = mouseWorldY;

            // Create and add bullet
//            gp.createBullet(gp.player.worldX, gp.player.worldY, mouseWorldX, mouseWorldY);
//            gp.shoot(gp.player.worldX, gp.player.worldY, mouseWorldX, mouseWorldY);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            isShooting = false;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isShooting) {
            mouseX = gp.camera.position.x - gp.screenWidth/2 +
                screenX * (float)gp.screenWidth / Gdx.graphics.getWidth();
            mouseY = gp.camera.position.y - gp.screenHeight/2 +
                (Gdx.graphics.getHeight() - screenY) * (float)gp.screenHeight / Gdx.graphics.getHeight();
        }
        return true;
    }
}
