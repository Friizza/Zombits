package com.zombits.main.zombie;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.zombits.main.GamePanel;

public class ZombieSpawner {
    private GamePanel gp;
    private long lastSpawnTime;
    private long spawnInterval = 1500; // 1.5 seconds between spawns
    private float spawnRadius = 15f; // In tiles
    private int maxZombies = 30;

    private long gameStartTime;
    public int difficultyLevel = 1;

    public ZombieSpawner(GamePanel gp) {
        this.gp = gp;
        this.lastSpawnTime = TimeUtils.millis();
        this.gameStartTime = TimeUtils.millis();
    }

    public void update() {
        // Update difficulty every 30 seconds
        long elapsedMinutes = (TimeUtils.millis() - gameStartTime) / 30000;
        if (elapsedMinutes + 1 > difficultyLevel) {
            difficultyLevel = (int)elapsedMinutes + 1;
            // Increase difficulty
            spawnInterval = Math.max(1000, 3000 - (difficultyLevel * 300)); // Spawn faster
            maxZombies = Math.min(50, 20 + (difficultyLevel * 5)); // More zombies allowed
            // Increase damage value of weapons
            gp.player.pistol.damage += 5;
            gp.player.rifle.damage += 5;
        }

        // Check if it's time to spawn and we haven't hit the zombie limit
        if (TimeUtils.millis() - lastSpawnTime > spawnInterval && gp.zombies.size() < maxZombies) {
            spawnZombie();
            lastSpawnTime = TimeUtils.millis();
        }
    }

    private void spawnZombie() {
        // Generate random angle
        float angle = MathUtils.random(360f);

        // Convert angle to radians
        float radians = (float)Math.toRadians(angle);

        // Calculate spawn position on circle around player
        float spawnX = gp.player.worldX + (float)Math.cos(radians) * (spawnRadius * GamePanel.tileSize);
        float spawnY = gp.player.worldY + (float)Math.sin(radians) * (spawnRadius * GamePanel.tileSize);

        // Validate spawn position
        if (isValidSpawnLocation(spawnX, spawnY)) {
            Zombie zombie = new Zombie(gp, spawnX, spawnY);
            zombie.loadTextures();
            gp.zombies.add(zombie);
        }
    }

    private boolean isValidSpawnLocation(float x, float y) {
        // Check if position is within map bounds
        int mapWidth = gp.map.getProperties().get("width", Integer.class) * GamePanel.tileSize;
        int mapHeight = gp.map.getProperties().get("height", Integer.class) * GamePanel.tileSize;

        if (x < 0 || x >= mapWidth || y < 0 || y >= mapHeight) {
            return false;
        }

        // Check if position collides with walls
        return !gp.cChecker.checkTileCollision(x, y);
    }

    // Getters and setters for customization
    public void setSpawnInterval(long milliseconds) {
        this.spawnInterval = milliseconds;
    }

    public void setSpawnRadius(float tilesRadius) {
        this.spawnRadius = tilesRadius;
    }

    public void setMaxZombies(int max) {
        this.maxZombies = max;
    }
}
