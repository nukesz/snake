package com.nukesz.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class BodyPart extends AbstractGameObject {


    public BodyPart(Texture texture) {
        super(0, 0, texture);
    }

    public BodyPart(int x, int y) {
        super(x, y, null);
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
