package com.zombits.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;

public class Data {

    GamePanel gp;

    public Data(GamePanel gp) {
        this.gp = gp;
    }

    public void saveHighScore(int score) {
        try {
            FileHandle file = Gdx.files.local("data.txt");

            int highScore = 0;
            if (file.exists()) {
                highScore = Integer.parseInt(file.readString().trim());
                System.out.println(highScore);
            }

            if(score >= highScore) {
                file.writeString(String.valueOf(score), false);
            }
        } catch (Exception e) {
            Gdx.app.log("Data", "Errore durante il salvataggio: " + e.getMessage());
        }
    }

    public String loadHighScore() {
        try {
            FileHandle file = Gdx.files.local("data.txt");
            if (file.exists()) {
                return file.readString().trim();
            } else {
                file.writeString("0", false);
                return "0";
            }
        } catch (Exception e) {
            Gdx.app.log("Data", "Errore durante il caricamento: " + e.getMessage());
            return "0";
        }
    }
}

