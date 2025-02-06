package com.zombits.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GamePanel extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;

    KeyHandler keyH = new KeyHandler(this);
    Player player = new Player(this);

    static final int originalTileSize = 16;
    static final int scale = 3;

    static final int tileSize = originalTileSize * scale;
    static final int maxScreenCol = 16;
    static final int maxScreenRow = 12;
    public static final int screenWidth = tileSize * maxScreenCol;
    public static final int screenHeight = tileSize * maxScreenRow;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");

        // LOAD PLAYER SPRITES
        player.downStill = new Texture("player/player_still_down.png");
        player.down1 = new Texture("player/player_down1.png");
        player.down2 = new Texture("player/player_down2.png");
        player.upStill = new Texture("player/player_still_up.png");
        player.up1 = new Texture("player/player_up1.png");
        player.up2 = new Texture("player/player_up2.png");
        player.leftStill = new Texture("player/player_still_left.png");
        player.left1 = new Texture("player/player_left1.png");
        player.left2 = new Texture("player/player_left2.png");
        player.rightStill = new Texture("player/player_still_right.png");
        player.right1 = new Texture("player/player_right1.png");
        player.right2 = new Texture("player/player_right2.png");

    }

    @Override
    public void render() {
        keyH.checkInput();
        update();
        draw();

    }

    public void update() {

        // UPDATE PLAYER SPRITE COUNTER
        player.spriteCounter++;

        if(keyH.downPressed || keyH.upPressed || keyH.leftPressed || keyH.rightPressed) {
            if(player.spriteCounter <= 60) {
                player.down = player.down1;
                player.up = player.up1;
                player.left = player.left1;
                player.right = player.right1;
            } else {
                player.down = player.down2;
                player.up = player.up2;
                player.left = player.left2;
                player.right = player.right2;
            }
        } else {
            player.down = player.downStill;
            player.up = player.upStill;
            player.left = player.leftStill;
            player.right = player.rightStill;
        }

        if(player.spriteCounter == 120) {
            player.spriteCounter = 0;
        }

    }

    public void draw() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        batch.begin();

        if(player.direction.equals("down")) {
            batch.draw(player.down, player.worldX, player.worldY, tileSize, tileSize);
        } else if(player.direction.equals("up")) {
            batch.draw(player.up, player.worldX, player.worldY, tileSize, tileSize);
        } else if(player.direction.equals("left")) {
            batch.draw(player.left, player.worldX, player.worldY, tileSize, tileSize);
        } else if(player.direction.equals("right")) {
            batch.draw(player.right, player.worldX, player.worldY, tileSize, tileSize);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
