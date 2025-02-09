package com.zombits.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class CoordinateUtility {

    private BitmapFont font;

    public CoordinateUtility(BitmapFont font) {
        this.font = font;
    }

    public int getCenteredTextX(String text) {
        GlyphLayout layout = new GlyphLayout(font, text);
        int screenWidth = Gdx.graphics.getWidth();
        return (int)((screenWidth - layout.width) / 2);
    }

    public int getCenteredTextY(String text) {
        GlyphLayout layout = new GlyphLayout(font, text);
        int screenHeight = Gdx.graphics.getHeight();
        return (int)((screenHeight + layout.height) / 2);
    }

    public int getCenteredImageX(int imageWidth) {
        int screenWidth = Gdx.graphics.getWidth();
        return (screenWidth - imageWidth) / 2;
    }

    public int getCenteredImageY(int imageHeight) {
        int screenHeight = Gdx.graphics.getHeight();
        return (screenHeight - imageHeight) / 2;
    }
}
