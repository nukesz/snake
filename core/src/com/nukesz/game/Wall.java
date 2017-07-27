package com.nukesz.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Wall {

    public int x;
    public int y;
    private Texture texture;

    public Wall(Texture texture, int x, int y) {
        this.texture = texture;
        this.x = x;
        this.y = y;
    }

    public void draw(Batch batch) {
        batch.draw(texture, x, y);
    }
}
