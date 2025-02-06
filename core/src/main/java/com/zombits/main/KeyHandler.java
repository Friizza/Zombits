package com.zombits.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyHandler extends Input.Keys {

    GamePanel gp;

    public boolean leftPressed, rightPressed, upPressed, downPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    public void checkInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            upPressed = true;
            gp.player.worldY++;
            gp.player.direction = "up";
        } else {
            upPressed = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            downPressed = true;
            gp.player.worldY--;
            gp.player.direction = "down";
        } else {
            downPressed = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            leftPressed = true;
            gp.player.worldX--;
            gp.player.direction = "left";
        } else {
            leftPressed = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            rightPressed = true;
            gp.player.worldX++;
            gp.player.direction = "right";
        } else {
            rightPressed = false;
        }
    }

}
