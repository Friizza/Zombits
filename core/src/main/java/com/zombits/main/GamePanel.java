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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    ShapeRenderer sRenderer;

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
    public ZombieSpawner zombieSpawner;
    CoordinateUtility coorUtil;
    public Data data = new Data(this);
    public BulletRefillManager refillManager;

    public ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    public Texture bulletTexture;
    public Texture reloadIndicator;

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
    public final int pauseState = 2;
    public final int gameOverState = 3;

    public int score = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        sRenderer = new ShapeRenderer();

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

        // Initialize bullet refill
        refillManager = new BulletRefillManager(this);

        // SET INPUT PROCESSOR
        Gdx.input.setInputProcessor(mouseH);

        // Load Textures
        loadTextures();
    }

    public void loadTextures() {
        // Load logo
        ui.logo = new Texture("logo.png");

        // Load player sprites
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

        // Load Crosshair and Bullet texture
        crosshair.image = new Texture("crosshair.png");
        bulletTexture = new Texture("bullet.png");

        // Load gun textures
        player.gunPistol = new Texture("Gun/pistol.png");
        player.gunRifle = new Texture("Gun/rifle.png");
        refillManager.refillTexture = new Texture("Gun/ammo_box.png");
        reloadIndicator = new Texture("Ui/reload.png");

        // Load UI Textures
        ui.uiBackground = new Texture("Ui/ui_background.png");
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
            checkGameOver();
            player.update(deltaTime);
            updateZombies();
            updateBullets();
            refillManager.update();

            // UPDATE CAMERA
            camera.position.set(player.worldX, player.worldY, 0);
            camera.update();

            System.out.println(player.worldX + " " + player.worldY);
        }

        // DEBUG
//        System.out.println(player.currentGun.getName());
//        System.out.println(player.health);
//        System.out.println("Player X: " + player.worldX + " Player Y: " + player.worldY);

    }
    void checkGameOver() {
        if(player.health <= 0) {
            gameState = gameOverState;
            data.saveHighScore(score);
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
            // Set the view for the renderer
            renderer.setView(camera.combined,
                camera.position.x - screenWidth/2 - tileSize * 5,
                camera.position.y - screenHeight/2 - tileSize * 5,
                screenWidth + tileSize * 10,
                screenHeight + tileSize * 10
            );

            // Draw first two layers (background and street/grass)
            int[] baseLayers = {0, 1};
            renderer.render(baseLayers);

            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            // Draw Bullets
            for (Bullet bullet : bullets) {
                batch.draw(
                    bullet.texture,
                    bullet.x,
                    bullet.y,
                    tileSize/4,
                    tileSize/4,
                    tileSize/2,
                    tileSize/2,
                    1,
                    1,
                    bullet.rotation,
                    0,
                    0,
                    bullet.texture.getWidth(),
                    bullet.texture.getHeight(),
                    false,
                    false
                );
            }
            batch.end();

            TiledMapTileLayer structureLayer = (TiledMapTileLayer) map.getLayers().get(2);

            // For each tile in the structure layer, check if player should be in front or behind
            int mapWidth = structureLayer.getWidth();
            int mapHeight = structureLayer.getHeight();

            int startX = Math.max(0, (int)((camera.position.x - screenWidth/2 - tileSize * 5) / tileSize));
            int endX = Math.min(mapWidth, (int)((camera.position.x + screenWidth/2 + tileSize * 5) / tileSize) + 1);
            int startY = Math.max(0, (int)((camera.position.y - screenHeight/2 - tileSize * 5) / tileSize));
            int endY = Math.min(mapHeight, (int)((camera.position.y + screenHeight/2 + tileSize * 5) / tileSize) + 1);

            // Structures that should be drawn before player
            TiledMap foregroundMap = new TiledMap();
            TiledMapTileLayer foregroundLayer = new TiledMapTileLayer(mapWidth, mapHeight,
                (int)structureLayer.getTileWidth(), (int)structureLayer.getTileHeight());
            foregroundMap.getLayers().add(foregroundLayer);

            // Structures that should be drawn after player
            TiledMap backgroundMap = new TiledMap();
            TiledMapTileLayer backgroundLayer = new TiledMapTileLayer(mapWidth, mapHeight,
                (int)structureLayer.getTileWidth(), (int)structureLayer.getTileHeight());
            backgroundMap.getLayers().add(backgroundLayer);

            for (int x = startX; x < endX; x++) {
                for (int y = startY; y < endY; y++) {
                    TiledMapTileLayer.Cell cell = structureLayer.getCell(x, y);
                    if (cell != null) {
                        float tileWorldY = y * tileSize;
                        float tileBottom = tileWorldY + tileSize;

                        if (player.worldY + tileSize > tileBottom) {
                            foregroundLayer.setCell(x, y, cell);
                        } else {
                            backgroundLayer.setCell(x, y, cell);
                        }
                    }
                }
            }

            OrthogonalTiledMapRenderer backgroundRenderer = new OrthogonalTiledMapRenderer(backgroundMap, scale);
            OrthogonalTiledMapRenderer foregroundRenderer = new OrthogonalTiledMapRenderer(foregroundMap, scale);

            backgroundRenderer.setView(camera.combined,
                camera.position.x - screenWidth/2 - tileSize * 5,
                camera.position.y - screenHeight/2 - tileSize * 5,
                screenWidth + tileSize * 10,
                screenHeight + tileSize * 10);
            foregroundRenderer.setView(camera.combined,
                camera.position.x - screenWidth/2 - tileSize * 5,
                camera.position.y - screenHeight/2 - tileSize * 5,
                screenWidth + tileSize * 10,
                screenHeight + tileSize * 10);

            // Draw background structures first
            backgroundRenderer.render();

            // Draw player and zombies sorted by Y position
            batch.begin();

            ArrayList<Renderable> renderables = new ArrayList<>();

            renderables.add(new Renderable(player.worldX, player.worldY + tileSize, "player"));

            for (Zombie zombie : zombies) {
                if (zombie.isAlive) {
                    renderables.add(new Renderable(zombie.worldX, zombie.worldY + tileSize, "zombie", zombies.indexOf(zombie)));
                }
            }

            refillManager.draw(batch);

            renderables.sort((r1, r2) -> Float.compare(r2.y, r1.y));

            // Draw entities in sorted order
            for (Renderable renderable : renderables) {
                if (renderable.type.equals("player")) {
                    // Draw player
                    if(player.isDamaged) {
                        batch.setColor(1, 0, 0, 1);
                    }
                    switch(player.spriteDirection) {
                        case "left":
                            batch.draw(player.left, player.worldX, player.worldY, tileSize, tileSize);
                            break;
                        case "right":
                            batch.draw(player.right, player.worldX, player.worldY, tileSize, tileSize);
                            break;
                    }
                    batch.setColor(1, 1, 1, 1);

                    // Draw Gun
                    float gunAngle = (float)Math.toDegrees(Math.atan2(
                        crosshair.y - player.worldY,
                        crosshair.x - player.worldX
                    ));

                    boolean flipTexture = crosshair.x < player.worldX;

                    batch.draw(
                        player.currentGun == player.pistol ? player.gunPistol : player.gunRifle,
                        player.worldX + tileSize/4,
                        player.worldY - tileSize/4,
                        tileSize/2,
                        tileSize/2,
                        tileSize,
                        tileSize,
                        1,
                        1,
                        gunAngle,
                        0,
                        0,
                        player.currentGun == player.pistol ? player.gunPistol.getWidth() : player.gunRifle.getWidth(),
                        player.currentGun == player.pistol ? player.gunPistol.getHeight() : player.gunRifle.getHeight(),
                        false,
                        flipTexture
                    );

                    drawReloadIndicator(batch);
                } else if (renderable.type.equals("zombie")) {
                    // Draw zombie
                    Zombie zombie = zombies.get(renderable.index);
                    if (zombie.direction.equals("left")) {
                        batch.draw(zombie.left, zombie.worldX, zombie.worldY, tileSize, tileSize);
                    } else {
                        batch.draw(zombie.right, zombie.worldX, zombie.worldY, tileSize, tileSize);
                    }
                }
            }

            // draw foreground structures
            batch.end();
            foregroundRenderer.render();

            // Draw crosshair
            batch.begin();
            batch.draw(crosshair.image, crosshair.x, crosshair.y, originalTileSize * 3, originalTileSize * 3);
            batch.end();

            backgroundRenderer.dispose();
            foregroundRenderer.dispose();

            /// DRAW UI ///
            batch.setProjectionMatrix(uiCamera.combined);
            batch.begin();

            // Score
            String scoreString = "Score: " + this.score;
            uiFont.draw(batch, scoreString, 20, Gdx.graphics.getHeight() - 20);
            // Difficulty Level
            String diffLevel = "Difficulty: " + zombieSpawner.difficultyLevel;
            uiFont.draw(batch, diffLevel, 20, Gdx.graphics.getHeight() - 60);

            // Draw UI Background
            batch.draw(ui.uiBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            String ammo = "Ammo: " + player.currentGun.ammo + " / " + player.currentGun.reserveAmmo;
            String hp = "HP: " + player.health;

            uiFont.draw(batch, ammo, 30, 50);
            uiFont.draw(batch, hp, 30, 95);

            batch.end();

//            // Draw black box over not selected gun
//            sRenderer.begin(ShapeRenderer.ShapeType.Filled);
//            sRenderer.setColor(new com.badlogic.gdx.graphics.Color(0 / 255f, 0 / 255f, 0 / 255f, 0.7f));
//            if(player.currentGun.getName().equals("Rifle")) {
//                sRenderer.rect(361 * scale, 4 * scale, 27 * scale, 17 * scale);
//            } else {
//                sRenderer.rect(331 * scale, 4 * scale, 25 * scale, 17 * scale);
//            }
//            sRenderer.end();
        }
        else if (gameState == pauseState) {
            sRenderer.begin(ShapeRenderer.ShapeType.Filled);
            sRenderer.setColor(new com.badlogic.gdx.graphics.Color(0 / 255f, 0 / 255f, 0 / 255f, 1f));
            sRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            sRenderer.end();

            batch.begin();
            menuFont.draw(batch, "GAME PAUSED", coorUtil.getCenteredMenuTextX("GAME PAUSED"), coorUtil.getCenteredMenuTextY("GAME PAUSED"));
            uiFont.draw(batch, "Press ESC to Resume", coorUtil.getCenteredUiTextX("Press ESC to Resume"), coorUtil.getCenteredUiTextY("Press ESC to Resume") / 2);
            batch.end();
        }
        else if (gameState == menuState) {
            batch.begin();

            String pressEnter = "Press Enter to Start";
            String highScore = "Highest Score: " + data.loadHighScore();

            // Draw Logo
            batch.draw(ui.logo, coorUtil.getCenteredImageX(300*4),
                coorUtil.getCenteredImageY(166*4) + coorUtil.getCenteredImageY(166*4) / 2, 300*4, 166*4);

            // Draw Press Enter to Start
            menuFont.draw(batch, pressEnter, coorUtil.getCenteredMenuTextX(pressEnter), coorUtil.getCenteredMenuTextY(pressEnter) - coorUtil.getCenteredMenuTextY(pressEnter) / 2);
            // Draw High Score
            uiFont.draw(batch, highScore, coorUtil.getCenteredUiTextX(highScore), coorUtil.getCenteredUiTextY(highScore) - coorUtil.getCenteredMenuTextY(highScore) / 3);

            String coontrols = "WASD to move - Left Click to shoot - 1 & 2 to switch guns - R to reload - ESC to pause";
            uiFont.draw(batch, coontrols, coorUtil.getCenteredUiTextX(coontrols), Gdx.graphics.getHeight() - 50);

            batch.end();
        }
        else if (gameState == gameOverState) {
            sRenderer.begin(ShapeRenderer.ShapeType.Filled);
            sRenderer.setColor(new com.badlogic.gdx.graphics.Color(0 / 255f, 0 / 255f, 0 / 255f, 1f));
            sRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            sRenderer.end();

            batch.begin();
            menuFont.draw(batch, "GAME OVER", coorUtil.getCenteredMenuTextX("GAME OVER"), coorUtil.getCenteredMenuTextY("GAME OVER"));
            String finalScore = "Score: " + score;
            menuFont.draw(batch, finalScore, coorUtil.getCenteredMenuTextX(finalScore), coorUtil.getCenteredMenuTextY(finalScore) / 2);
            String pressEnter = "Press Enter to go back to main menu";
            uiFont.draw(batch, pressEnter, coorUtil.getCenteredUiTextX(pressEnter), coorUtil.getCenteredUiTextY(pressEnter) / 2 - 100);
            batch.end();
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
        refillManager.dispose();
        reloadIndicator.dispose();
    }

    //// HELPER METHODS ////
    // Player
    public void shoot(float startX, float startY, float targetX, float targetY) {
        if(!keyH.isReloading) {
            if(player.currentGun.ammo > 0) {
                Bullet bullet = new Bullet(this, startX + tileSize / 2, startY, targetX, targetY);
                bullet.texture = bulletTexture;
                bullets.add(bullet);
                player.currentGun.ammo--;
                gameSound.playSE(gameSound.shoot);
            } else if(player.currentGun.ammo == 0) {
                gameSound.playSE(gameSound.noAmmo);
            }
        }
    }

    // Bullets
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

    // Reload
    private void drawReloadIndicator(SpriteBatch batch) {
        if (keyH.isReloading) {
            float rotation = (System.currentTimeMillis() % 1500) / 1500f * 360f;

            // Position slightly above player
            float indicatorX = player.worldX + (tileSize/2) - (tileSize/4);
            float indicatorY = player.worldY + tileSize + (tileSize/4);

            // Draw rotating indicator
            batch.draw(
                reloadIndicator,
                indicatorX,
                indicatorY,
                tileSize/4,
                tileSize/4,
                tileSize/2,
                tileSize/2,
                1,
                1,
                rotation,
                0,
                0,
                reloadIndicator.getWidth(),
                reloadIndicator.getHeight(),
                false,
                false
            );
        }
    }
}
