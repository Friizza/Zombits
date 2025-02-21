package com.zombits.main;

import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;
import java.util.Random;

public class BulletRefillManager {
    private GamePanel gp;
    private ArrayList<BulletRefill> refills = new ArrayList<>();
    public Texture refillTexture;
    private Random random = new Random();
    private final int MAX_REFILLS = 2;
    private final int MAP_WIDTH = 40;
    private final int MAP_HEIGHT = 40;

    public BulletRefillManager(GamePanel gp) {
        this.gp = gp;
    }

    public void startSpawning() {
        spawnInitialRefills();
    }

    private void spawnInitialRefills() {
        // Spawn initial refills
        for (int i = 0; i < MAX_REFILLS; i++) {
            spawnNewRefill();
        }
    }

    public void spawnNewRefill() {
        int x, y;
        boolean validPosition;

        do {
            // Generate random position
            x = random.nextInt(MAP_WIDTH) * GamePanel.tileSize;
            y = random.nextInt(MAP_HEIGHT) * GamePanel.tileSize;

            validPosition = true;

            // Check if position is not colliding with any tile
            if (gp.cChecker.checkTileCollision(x, y)) {
                validPosition = false;
                continue;
            }

            // Avoid spawning refills too close to the player
            float distToPlayer = (float)Math.sqrt(
                Math.pow(x - gp.player.worldX, 2) +
                    Math.pow(y - gp.player.worldY, 2)
            );

            if (distToPlayer < 5 * GamePanel.tileSize) {
                validPosition = false;
            }

            // Check if not overlapping existing refills
            for (BulletRefill refill : refills) {
                if (refill.isActive) {
                    float dist = (float)Math.sqrt(
                        Math.pow(x - refill.worldX, 2) +
                            Math.pow(y - refill.worldY, 2)
                    );

                    if (dist < 2 * GamePanel.tileSize) {
                        validPosition = false;
                        break;
                    }
                }
            }

        } while (!validPosition);

        // Create new refill at valid position
        BulletRefill refill = new BulletRefill(gp, x, y);
        refill.texture = refillTexture;
        refills.add(refill);
    }

    public void update() {
        for (int i = refills.size() - 1; i >= 0; i--) {
            BulletRefill refill = refills.get(i);
            refill.update();

            if (!refill.isActive) {
                refills.remove(i);
                spawnNewRefill();
            }
        }
    }

    public void draw(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
        for (BulletRefill refill : refills) {
            if (refill.isActive) {
                batch.draw(refill.texture, refill.worldX, refill.worldY, GamePanel.tileSize, GamePanel.tileSize);
            }
        }
    }

    public void dispose() {
        if (refillTexture != null) {
            refillTexture.dispose();
        }

        for (BulletRefill refill : refills) {
            refill.dispose();
        }
    }
}
