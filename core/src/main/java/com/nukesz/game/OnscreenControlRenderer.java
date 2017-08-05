package com.nukesz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class OnscreenControlRenderer {
    SpriteBatch batch;
    TextureRegion left;
    TextureRegion right;
    //Texture up;
    TextureRegion down;
    TextureRegion up;

    public OnscreenControlRenderer() {
        loadAssets();
    }

    private void loadAssets() {
        //TODO make this in the proper way
        Texture texture = new Texture(Gdx.files.internal("sidearrow.png"));
        Texture texture2 = new Texture(Gdx.files.internal("topbottonarrow.png"));
       // up = new Texture(Gdx.files.internal("uparrow.png"));
        TextureRegion[] buttons = TextureRegion.split(texture, 30, 480)[0];
        TextureRegion[] buttons2 = TextureRegion.split(texture2, 640, 30)[0];

        left = buttons[1];
        right = buttons[0];
        up = buttons2[0];
        buttons2 = TextureRegion.split(texture2, 640, 30)[1];
        down= buttons2[0];
        batch = new SpriteBatch();
        //	batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
    }

    public void render() {
        //if (Gdx.app.getType() != ApplicationType.Android && Gdx.app.getType() != ApplicationType.iOS) return;

        batch.begin();
        batch.draw(left, 1, 0);
        batch.draw(right, 609, 0);
        batch.draw(up, 0, 480 - 31);
        batch.draw(down, 0, 1);
        batch.end();
    }

    public void dispose() {
        batch.dispose();
    }
}
