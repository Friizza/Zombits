package com.zombits.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class CoordinateUtility {

    private BitmapFont menuFont;
    private BitmapFont uiFont;

    public CoordinateUtility(BitmapFont menuFont, BitmapFont uiFont) {
        this.menuFont = menuFont;
        this.uiFont = uiFont;
    }

    //// MENU FONT
    public int getCenteredMenuTextX(String text) {
        GlyphLayout layout = new GlyphLayout(menuFont, text);
        int screenWidth = Gdx.graphics.getWidth();
        return (int)((screenWidth - layout.width) / 2);
    }

    public int getCenteredMenuTextY(String text) {
        GlyphLayout layout = new GlyphLayout(menuFont, text);
        int screenHeight = Gdx.graphics.getHeight();
        return (int)((screenHeight + layout.height) / 2);
    }

    //// UI FONT
    public int getCenteredUiTextX(String text) {
        GlyphLayout layout = new GlyphLayout(uiFont, text);
        int screenWidth = Gdx.graphics.getWidth();
        return (int)((screenWidth - layout.width) / 2);
    }

    public int getCenteredUiTextY(String text) {
        GlyphLayout layout = new GlyphLayout(uiFont, text);
        int screenHeight = Gdx.graphics.getHeight();
        return (int)((screenHeight + layout.height) / 2);
    }

    //// IMAGES
    public int getCenteredImageX(int imageWidth) {
        int screenWidth = Gdx.graphics.getWidth();
        return (screenWidth - imageWidth) / 2;
    }

    public int getCenteredImageY(int imageHeight) {
        int screenHeight = Gdx.graphics.getHeight();
        return (screenHeight - imageHeight) / 2;
    }
}
