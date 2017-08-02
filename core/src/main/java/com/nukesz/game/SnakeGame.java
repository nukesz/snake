package com.nukesz.game;

import com.badlogic.gdx.Game;
import com.nukesz.game.screen.MenuScreen;

public class SnakeGame extends Game {

    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }

}
