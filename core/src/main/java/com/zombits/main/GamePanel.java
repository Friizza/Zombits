package com.zombits.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GamePanel extends ApplicationAdapter {

    private SpriteBatch batch;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;

    Player player = new Player(this);
    KeyHandler keyH = new KeyHandler(this);
    MouseHandler mouseH = new MouseHandler(this);
    CollisionChecker cChecker = new CollisionChecker(this);
    Crosshair crosshair = new Crosshair(this);

    static final int originalTileSize = 16;
    static final int scale = 5; // 3 PER 1080P, 7 PER 4K

    static final int tileSize = originalTileSize * scale;
    static final int maxScreenCol = 26;
    static final int maxScreenRow = 18;
    public static final int screenWidth = tileSize * maxScreenCol;
    public static final int screenHeight = tileSize * maxScreenRow;

    public int gameState = 1;
    public final int menuState = 0;
    public final int playState = 1;
    public final int inventoryState = 2;
    public final int pauseState = 3;
    public final int gameOverState = 4;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // LOAD MAP
        map = new TmxMapLoader().load("Maps/world01.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, scale);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        // SET INPUT PROCESSOR
        Gdx.input.setInputProcessor(mouseH);

        // LOAD PLAYER SPRITES
        player.rightStill1 = new Texture("player/player_right_still_1.png");
        player.rightStill2 = new Texture("player/player_right_still_2.png");
        player.rightStill3 = new Texture("player/player_right_still_3.png");
        player.right1 = new Texture("player/player_right_1.png");
        player.right2 = new Texture("player/player_right_2.png");
        player.right3 = new Texture("player/player_right_3.png");
        player.right4 = new Texture("player/player_right_4.png");
        player.leftStill1 = new Texture("player/player_left_still_1.png");
        player.leftStill2 = new Texture("player/player_left_still_2.png");
        player.leftStill3 = new Texture("player/player_left_still_3.png");
        player.left1 = new Texture("player/player_left_1.png");
        player.left2 = new Texture("player/player_left_2.png");
        player.left3 = new Texture("player/player_left_3.png");
        player.left4 = new Texture("player/player_left_4.png");

        // LOAD CROSSHAIR TEXTURE
        crosshair.image = new Texture("crosshair.png");

    }

    @Override
    public void render() {
        keyH.checkInput();
        update();
        draw();
    }

    public void update() {

        // MENU STATE

        // GAME STATE
        if(gameState == playState) {
            updateCrosshair();
            updatePlayerSprite();

            // UPDATE CAMERA
            camera.position.set(player.worldX, player.worldY, 0);
            camera.update();
        }

    }
    public void updatePlayerSprite() {
        player.spriteCounter++;
        if(keyH.downPressed || keyH.upPressed || keyH.leftPressed || keyH.rightPressed) {
            if(player.spriteCounter <= 15) {
                player.left = player.left1;
                player.right = player.right1;
            } else if (player.spriteCounter <= 30) {
                player.left = player.left2;
                player.right = player.right2;
            } else if(player.spriteCounter <= 45) {
                player.left = player.left3;
                player.right = player.right3;
            } else if (player.spriteCounter <= 60) {
                player.left = player.left4;
                player.right = player.right4;
            } else if (player.spriteCounter <= 75) {
                player.left = player.left1;
                player.right = player.right1;
            } else if(player.spriteCounter <= 90) {
                player.left = player.left2;
                player.right = player.right2;
            } else if (player.spriteCounter <= 105) {
                player.left = player.left3;
                player.right = player.right3;
            } else {
                player.left = player.left4;
                player.right = player.right4;
            }
        } else {
            if(player.spriteCounter <= 40) {
                player.left = player.leftStill1;
                player.right = player.rightStill1;
            } else if (player.spriteCounter <= 80) {
                player.left = player.leftStill2;
                player.right = player.rightStill2;
            } else {
                player.left = player.leftStill3;
                player.right = player.rightStill3;
            }
        }

        if(player.spriteCounter == 120) {
            player.spriteCounter = 0;
        }
    }
    public void updateCrosshair() {
        // Calculate mouse world position
        float mouseWorldX = camera.position.x - screenWidth/2 +
            Gdx.input.getX() * (float)screenWidth / Gdx.graphics.getWidth();
        float mouseWorldY = camera.position.y - screenHeight/2 +
            (Gdx.graphics.getHeight() - Gdx.input.getY()) * (float)screenHeight / Gdx.graphics.getHeight();

        // Calculate direction vector to mouse
        float dirX = mouseWorldX - player.worldX;
        float dirY = mouseWorldY - player.worldY;

        // Normalize direction
        float length = (float)Math.sqrt(dirX * dirX + dirY * dirY);

        // Define circle radius
        float circleRadius = tileSize * 1.5f;

        // Calculate crosshair position on circle
        crosshair.x = (int)(player.worldX + (dirX / length) * circleRadius);
        crosshair.y = (int)(player.worldY + (dirY / length) * circleRadius);
    }

    public void resize(int width, int height) {
        if(gameState == playState) {
            camera.viewportHeight = height;
            camera.viewportWidth = width;
            camera.update();
        }
    }

    public void draw() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        if(gameState == playState) {
            // DRAW MAP
            renderer.setView(camera.combined,
                camera.position.x - screenWidth/2 - tileSize * 5,
                camera.position.y - screenHeight/2 - tileSize * 5,
                screenWidth + tileSize * 10,
                screenHeight + tileSize * 10
            );
            renderer.render();

            //
            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            // DRAW PLAYER
            switch(player.spriteDirection) {
                case "left":
                    batch.draw(player.left, player.worldX, player.worldY, tileSize, tileSize);
                    break;
                case "right":
                    batch.draw(player.right, player.worldX, player.worldY, tileSize, tileSize);
                    break;
            }

            // DRAW CROSSHAIR
            batch.draw(crosshair.image, crosshair.x, crosshair.y, originalTileSize * 3, originalTileSize * 3);

            batch.end();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        map.dispose();
        renderer.dispose();

        player.dispose();
        crosshair.dispose();
    }
}
