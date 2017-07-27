package com.nukesz.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class BodyPart {
    public int x;
    public int y;
    private Texture texture;

    public BodyPart(Texture texture) {
        this.texture = texture;
    }

    public void updateBodyPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Batch batch, int snakeX, int snakeY) {
        if (x != snakeX || y != snakeY) {
            batch.draw(texture, x, y);
        }
    }

}
