package com.zombits.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class GameSound {

    GamePanel gp;

    Sound walk = Gdx.audio.newSound(Gdx.files.internal("Sound/walking.wav"));
    Sound shoot = Gdx.audio.newSound(Gdx.files.internal("Sound/shoot.wav"));
//    Music music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

    public GameSound(GamePanel gp) {
        this.gp = gp;
    }

    public void playSE(Sound sound) {
        sound.play(1.0f);
    }

    public void playMusic() {

    }

    public void dispose() {
        walk.dispose();
        shoot.dispose();
    }
}
