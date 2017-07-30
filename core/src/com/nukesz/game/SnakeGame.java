package com.nukesz.game;

import com.badlogic.gdx.Game;

public class SnakeGame extends Game {

    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }

}
