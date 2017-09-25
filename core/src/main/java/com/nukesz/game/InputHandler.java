package com.nukesz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

public class InputHandler {

    public int width;
    public int height;

    public InputHandler(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Direction handleMouseClick(Camera camera) {

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (touchPos.x < 32 && touchPos.x > 0) {
                return Direction.LEFT;
            }
            if (touchPos.x < width && touchPos.x > width - 32) {
                return Direction.RIGHT;
            }
            if (touchPos.y < 32 && touchPos.y > 0) {
                return Direction.DOWN;
            }
            if (touchPos.y < height && touchPos.y > height - 32) {
                return Direction.UP;
            }
        }
        return Direction.NONE;
    }

    public void updateSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

}
