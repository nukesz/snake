package com.nukesz.game.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.nukesz.game.SnakeGame;

public class AbstractGameScreen extends ScreenAdapter {

    protected final SnakeGame game;

    public AbstractGameScreen(SnakeGame game) {
        this.game = game;
    }
}
