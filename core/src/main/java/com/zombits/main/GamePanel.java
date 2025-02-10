package com.zombits.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.zombits.main.zombie.Zombie;
import com.zombits.main.zombie.ZombieSpawner;

import javax.script.ScriptEngine;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GamePanel extends ApplicationAdapter {

    private SpriteBatch batch;
    public TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    OrthographicCamera uiCamera;

    // Menu Font
    FreeTypeFontGenerator menuGenerator;
    FreeTypeFontGenerator.FreeTypeFontParameter menuParameter;
    public BitmapFont menuFont;
    // UI Font
    FreeTypeFontGenerator uiGenerator;
    FreeTypeFontGenerator.FreeTypeFontParameter uiParameter;
    public BitmapFont uiFont;

    Random random = new Random();
    public Player player = new Player(this);
    KeyHandler keyH = new KeyHandler(this);
    MouseHandler mouseH = new MouseHandler(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    Crosshair crosshair = new Crosshair(this);
    UI ui = new UI(this);
    public GameSound gameSound;
    ZombieSpawner zombieSpawner;
    CoordinateUtility coorUtil;

    public ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    public Texture bulletTexture;

    public ArrayList<Zombie> zombies = new ArrayList<>();

    static final int originalTileSize = 16;
    static final int scale = 5;

    public static final int tileSize = originalTileSize * scale;
    static final int maxScreenCol = 24;
    static final int maxScreenRow = 14;
    public static final int screenWidth = tileSize * maxScreenCol;
    public static final int screenHeight = tileSize * maxScreenRow;

    public int gameState = 0;
    public final int menuState = 0;
    public final int playState = 1;
    public final int inventoryState = 2;
    public final int pauseState = 3;
    public final int gameOverState = 4;

    public int score = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Initialize GameSound
        gameSound = new GameSound(this);

        // Initialize font
        menuGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ByteBounce.ttf"));
        menuParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        menuParameter.size = 130;
        menuFont = menuGenerator.generateFont(menuParameter);

        uiGenerator = new FreeTypeFontGenerator(Gdx.files.internal("monogram.ttf"));
        uiParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        uiParameter.size = 50;
        uiFont = uiGenerator.generateFont(uiParameter);

        // Initialize CoordinateUtility
        coorUtil = new CoordinateUtility(menuFont, uiFont);

        // Initialize Map and Camera
        map = new TmxMapLoader().load("Maps/world01.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, scale);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Set up zombie spawner
        zombieSpawner = new ZombieSpawner(this);
        zombieSpawner.setSpawnInterval(2000);  // Spawn every 2 seconds
        zombieSpawner.setSpawnRadius(10);      // Spawn 10 tiles away from player
        zombieSpawner.setMaxZombies(30);       // Allow up to 30 zombies

        // SET INPUT PROCESSOR
        Gdx.input.setInputProcessor(mouseH);

        // Load Textures
        loadTextures();
    }

    public void loadTextures() {
        // LOAD LOGO
        ui.logo = new Texture("logo.png");

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

        // LOAD CROSSHAIR & BULLET TEXTURE
        crosshair.image = new Texture("crosshair.png");
        bulletTexture = new Texture("bullet.png");

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
            float deltaTime = Gdx.graphics.getDeltaTime();

            updateCrosshair();
            updatePlayerSprite();
            player.update(deltaTime);
            updateZombies();
            updateBullets();

            // UPDATE CAMERA
            camera.position.set(player.worldX, player.worldY, 0);
            camera.update();
        }

        // DEBUG
//        System.out.println(player.currentGun.getName());
//        System.out.println(player.health);
//        System.out.println("Player X: " + player.worldX + " Player Y: " + player.worldY);

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
    public void updateBullets() {
        for(int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.update();

            if (isOutOfBounds(bullet) || checkBulletCollision(bullet)) {
                bullets.remove(i);
            }
        }
    }
    public void updateZombies() {
        zombieSpawner.update();
        for (Zombie zombie : zombies) {
            zombie.update();
        }

        for (int i = zombies.size() - 1; i >= 0; i--) {
            if (!zombies.get(i).isAlive) {
                Zombie zombie = zombies.remove(i);
                zombie.dispose();
            }
        }
    }

    public void resize(int width, int height) {
        if(gameState == playState) {
            camera.viewportHeight = height;
            camera.viewportWidth = width;
            camera.update();

            uiCamera.viewportWidth = width;
            uiCamera.viewportHeight = height;
            uiCamera.position.set(width/2, height/2, 0);
            uiCamera.update();
        }
    }

    public void draw() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        if(gameState == playState) {
            /// DRAW GAME ///
            // Map
            renderer.setView(camera.combined,
                camera.position.x - screenWidth/2 - tileSize * 5,
                camera.position.y - screenHeight/2 - tileSize * 5,
                screenWidth + tileSize * 10,
                screenHeight + tileSize * 10
            );
            renderer.render();


            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            // Draw Bullets Accounting for rotation
            for (Bullet bullet : bullets) {
                batch.draw(
                    bullet.texture,           // texture
                    bullet.x,                 // x position
                    bullet.y,                 // y position
                    tileSize/4,              // origin x (half of width)
                    tileSize/4,              // origin y (half of height)
                    tileSize/2,              // width
                    tileSize/2,              // height
                    1,                       // scale x
                    1,                       // scale y
                    bullet.rotation,         // rotation (degrees)
                    0,                       // src x
                    0,                       // src y
                    bullet.texture.getWidth(),  // src width
                    bullet.texture.getHeight(), // src height
                    false,                   // flip x
                    false                    // flip y
                );
            }

            // Player
            switch(player.spriteDirection) {
                case "left":
                    batch.draw(player.left, player.worldX, player.worldY, tileSize, tileSize);
                    break;
                case "right":
                    batch.draw(player.right, player.worldX, player.worldY, tileSize, tileSize);
                    break;
            }

            // Zombies
            for (Zombie zombie : zombies) {
                if (zombie.isAlive) {
                    if (zombie.direction.equals("left")) {
                        batch.draw(zombie.left, zombie.worldX, zombie.worldY, tileSize, tileSize);
                    } else {
                        batch.draw(zombie.right, zombie.worldX, zombie.worldY, tileSize, tileSize);
                    }
                }
            }

            // Crosshair
            batch.draw(crosshair.image, crosshair.x, crosshair.y, originalTileSize * 3, originalTileSize * 3);

            batch.end();

            /// DRAW UI ///
            batch.setProjectionMatrix(uiCamera.combined);
            batch.begin();

            // Score
            String scoreString = "Score: " + this.score;
            uiFont.draw(batch, scoreString, 20, Gdx.graphics.getHeight() - 20);

            batch.end();
        }
        else if (gameState == inventoryState) {
            // Draw Inventory
        }
        else if (gameState == pauseState) {
            // Draw Pause Menu
        }
        else if (gameState == menuState) {
            batch.begin();

            String pressEnter = "Press Enter to Start";

            // Draw Logo
            batch.draw(ui.logo, coorUtil.getCenteredImageX(300*4),
                coorUtil.getCenteredImageY(166*4) + coorUtil.getCenteredImageY(166*4) / 2, 300*4, 166*4);

            // Draw Press Enter to Start
            menuFont.draw(batch, pressEnter, coorUtil.getCenteredMenuTextX(pressEnter), coorUtil.getCenteredMenuTextY(pressEnter) - coorUtil.getCenteredMenuTextY(pressEnter) / 2);

            batch.end();
        }
        else if (gameState == gameOverState) {
            // Draw Game Over Screen
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        map.dispose();
        renderer.dispose();

        player.dispose();
        ui.dispose();
        crosshair.dispose();
        for (Bullet bullet : bullets) {
            bullet.dispose();
        }
        for (Zombie zombie : zombies) {
            zombie.dispose();
        }
        bulletTexture.dispose();

        gameSound.dispose();
        menuGenerator.dispose();
        uiGenerator.dispose();
    }

    //// HELPER METHODS ////
    // Player
    public void shoot(float startX, float startY, float targetX, float targetY) {
        createBullet(startX, startY, targetX, targetY);
        gameSound.playSE(gameSound.shoot);
    }

    // Bullets
    public void createBullet(float startX, float startY, float targetX, float targetY) {
        Bullet bullet = new Bullet(this, startX, startY, targetX, targetY);
        bullet.texture = bulletTexture;
        bullets.add(bullet);
    }
    private boolean isOutOfBounds(Bullet bullet) {
        return bullet.x < 0 || bullet.x > map.getProperties().get("width", Integer.class) * tileSize ||
            bullet.y < 0 || bullet.y > map.getProperties().get("height", Integer.class) * tileSize;
    }
    private boolean checkBulletCollision(Bullet bullet) {
        // Check tile collision first
        if (cChecker.checkTileCollision(bullet.x, bullet.y)) {
            return true;
        }

        // Check zombie collisions
        Rectangle bulletRect = new Rectangle((int)bullet.x, (int)bullet.y, tileSize/2, tileSize/2);
        for (Zombie zombie : zombies) {
            if (zombie.isAlive) {
                Rectangle zombieRect = new Rectangle(
                    (int)(zombie.worldX + zombie.solidArea.x),
                    (int)(zombie.worldY + zombie.solidArea.y),
                    zombie.solidArea.width,
                    zombie.solidArea.height
                );

                if (bulletRect.intersects(zombieRect)) {
                    zombie.takeDamage(25); //// CHANGE?
                    return true;
                }
            }
        }

        return false;
    }
}
