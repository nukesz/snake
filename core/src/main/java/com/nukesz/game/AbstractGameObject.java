package com.nukesz.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class AbstractGameObject {

    public int x;
    public int y;
    public Texture texture;

    public AbstractGameObject(int x, int y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
    }

    public void draw(Batch batch) {
        batch.draw(texture, x, y);
    }
}
