package com.zombits.main;

public class Renderable {
    float x, y;
    String type;
    int index; // Used for zombies to reference back to the zombies list

    public Renderable(float x, float y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.index = -1;
    }

    public Renderable(float x, float y, String type, int index) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.index = index;
    }
}
