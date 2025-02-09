package com.zombits.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyHandler extends Input.Keys {

    GamePanel gp;

    private long lastWalkSoundTime = 0;
    private static final long WALK_SOUND_INTERVAL = 260;

    public boolean leftPressed, rightPressed, upPressed, downPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    public void checkInput() {

        if(gp.gameState == gp.playState) {
            float nextX = gp.player.worldX;
            float nextY = gp.player.worldY;

            if(Gdx.input.isKeyPressed(Input.Keys.W)) {
                upPressed = true;
                gp.player.direction = "up";
                nextY += gp.player.speed;
                playWalkSound();
                if(!gp.cChecker.checkTileCollision(nextX, nextY)) {
                    gp.player.worldY += gp.player.speed;
                }
            } else {
                upPressed = false;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.S)) {
                downPressed = true;
                gp.player.direction = "down";
                nextY -= gp.player.speed;
                playWalkSound();
                if(!gp.cChecker.checkTileCollision(nextX, nextY)) {
                    gp.player.worldY -= gp.player.speed;
                }
            } else {
                downPressed = false;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.A)) {
                leftPressed = true;
                gp.player.spriteDirection = "left";
                nextX -= gp.player.speed;
                playWalkSound();
                gp.player.direction = "left";
                if(!gp.cChecker.checkTileCollision(nextX, nextY)) {
                    gp.player.worldX -= gp.player.speed;
                }
            } else {
                leftPressed = false;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.D)) {
                rightPressed = true;
                gp.player.spriteDirection = "right";
                nextX += gp.player.speed;
                playWalkSound();
                gp.player.direction = "right";
                if(!gp.cChecker.checkTileCollision(nextX, nextY)) {
                    gp.player.worldX += gp.player.speed;
                }
            } else {
                rightPressed = false;
            }
        }
        else if(gp.gameState == gp.menuState) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                gp.gameState = gp.playState;
            }
        }
    }

    private void playWalkSound() {
        long currentTime = System.currentTimeMillis();

        if(currentTime - lastWalkSoundTime >= WALK_SOUND_INTERVAL) {
            gp.gameSound.playSE(gp.gameSound.walk);
            lastWalkSoundTime = currentTime;
        }
    }

}
