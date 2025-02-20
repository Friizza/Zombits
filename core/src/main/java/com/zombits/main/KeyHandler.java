package com.zombits.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.Timer;
import java.util.TimerTask;

public class KeyHandler extends Input.Keys {

    GamePanel gp;

    private long lastWalkSoundTime = 0;
    private static final long WALK_SOUND_INTERVAL = 260;
    public boolean isReloading = false;

    public boolean leftPressed, rightPressed, upPressed, downPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    public void checkInput() {

        if(gp.gameState == gp.playState) {
            float nextX = gp.player.worldX;
            float nextY = gp.player.worldY;

            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                gp.player.currentGun = gp.player.rifle;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                gp.player.currentGun = gp.player.pistol;
            }
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
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                if(gp.player.currentGun.ammo < gp.player.currentGun.magazineSize) {
                    if(gp.player.currentGun.reserveAmmo >= gp.player.currentGun.magazineSize) {
                        gp.player.currentGun.reserveAmmo -= (gp.player.currentGun.magazineSize - gp.player.currentGun.ammo);
                        gp.player.currentGun.ammo = gp.player.currentGun.magazineSize;
                        gp.gameSound.playSE(gp.gameSound.reload);
                        reloadTimer();
                    } else if(gp.player.currentGun.reserveAmmo > 0) {
                        reloadTimer();
                        while(gp.player.currentGun.reserveAmmo > 0) {
                            gp.player.currentGun.reserveAmmo--;
                            gp.player.currentGun.ammo++;
                            if(gp.player.currentGun.ammo == gp.player.currentGun.magazineSize) {
                                break;
                            }
                        }
                        gp.gameSound.playSE(gp.gameSound.reload);
                    }
                }
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                gp.gameState = gp.pauseState;
            }
        }
        else if(gp.gameState == gp.menuState) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                gp.gameState = gp.playState;
            }
        }
        else if(gp.gameState == gp.pauseState) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                gp.gameState = gp.playState;
            }
        }
    }

    private void reloadTimer() {
        isReloading = true;

        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                isReloading = false;
            }
        }, 1.5f); // 1.5 seconds reload
    }

    private void playWalkSound() {
        long currentTime = System.currentTimeMillis();

        if(currentTime - lastWalkSoundTime >= WALK_SOUND_INTERVAL) {
            gp.gameSound.playSE(gp.gameSound.walk);
            lastWalkSoundTime = currentTime;
        }
    }

}
