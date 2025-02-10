package com.zombits.main;

import com.badlogic.gdx.Gdx;

public class Gun {
    private String name;
    private int damage;
    public float fireRate;
    private boolean isAutomatic;
    public float lastShotTime;

    public Gun(String name, int damage, float fireRate, boolean isAutomatic) {
        this.name = name;
        this.damage = damage;
        this.fireRate = fireRate;
        this.isAutomatic = isAutomatic;
        this.lastShotTime = 0;
    }

    // Getters
    public String getName() { return name; }
    public int getDamage() { return damage; }
    public boolean isAutomatic() { return isAutomatic; }
    public float getFireRate() { return fireRate; }
}
