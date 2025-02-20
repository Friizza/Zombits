package com.zombits.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class GameSound {

    GamePanel gp;

    Sound walk = Gdx.audio.newSound(Gdx.files.internal("Sound/walking.wav"));
    Sound shoot = Gdx.audio.newSound(Gdx.files.internal("Sound/shoot.wav"));
    public Sound zombieGroan = Gdx.audio.newSound(Gdx.files.internal("Sound/zombie_groan.wav"));
    public Sound receiveDamage = Gdx.audio.newSound(Gdx.files.internal("Sound/receive_damage.wav"));
    public Sound noAmmo = Gdx.audio.newSound(Gdx.files.internal("Sound/no_ammo.wav"));
    public Sound reload = Gdx.audio.newSound(Gdx.files.internal("Sound/reload_gun.wav"));
//    Music music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

    public GameSound(GamePanel gp) {
        this.gp = gp;
    }

    public void playSE(Sound sound) {
        sound.play(0.5f);
    }

    public void playMusic() {

    }

    public void dispose() {
        walk.dispose();
        shoot.dispose();
        zombieGroan.dispose();
        receiveDamage.dispose();
        noAmmo.dispose();
        reload.dispose();
    }
}
