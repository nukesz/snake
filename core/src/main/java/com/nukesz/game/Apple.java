package com.nukesz.game;

import com.badlogic.gdx.graphics.Texture;

public class Apple extends AbstractGameObject {
    public boolean isAppleAvailable = false;

    public Apple(int x, int y, Texture texture) {
        super(x, y, texture);
    }
}
