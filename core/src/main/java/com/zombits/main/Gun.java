package com.zombits.main;

import com.badlogic.gdx.Gdx;

public class Gun {
    private String name;
    public int damage;
    public float fireRate;
    private boolean isAutomatic;
    public float lastShotTime;
    public int magazineSize;
    public int ammo;
    public int reserveAmmo;

    public Gun(String name, int damage, float fireRate, boolean isAutomatic,int magazineSize, int reserveAmmo) {
        this.name = name;
        this.damage = damage;
        this.fireRate = fireRate;
        this.isAutomatic = isAutomatic;
        this.lastShotTime = 0;
        this.magazineSize = magazineSize;
        this.ammo = magazineSize;
        this.reserveAmmo = reserveAmmo;
    }

    // Getters
    public String getName() { return name; }
    public int getDamage() { return damage; }
    public boolean isAutomatic() { return isAutomatic; }
    public float getFireRate() { return fireRate; }
}
