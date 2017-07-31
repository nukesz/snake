package com.nukesz.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Item {
    protected int x;
    protected int y;
    protected Texture texture;

    public Item(int x, int y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Batch batch) {
        batch.draw(texture, x, y);
    }
}
