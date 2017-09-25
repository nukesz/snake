package com.nukesz.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nukesz.game.*;

import java.awt.*;

public class GameScreen extends AbstractGameScreen {

    private static final int BOARD_WIDTH = 640;
    private static final int BOARD_HEIGHT = 480;
    private static final int GRID_CELL = 32;
    private static final int WORLD_WIDTH = BOARD_WIDTH + 2 * GRID_CELL;
    private static final int WORLD_HEIGHT = BOARD_HEIGHT + 2 * GRID_CELL;

    private static final String GAME_OVER_TEXT = "Game Over... Tap space to restart!";
    private static final String GAME_START_TEXT = "Welcome to the SNAKE game! Tap space to start!";
    private static final int POINTS_PER_APPLE = 10;


    private static final float MOVE_TIME = 0.2F;
    private static final int SNAKE_MOVEMENT = 32;
    Apple appleObj;
    OnscreenControlRenderer controlRenderer;
    private Direction snakeDirection = Direction.UP;
    private Viewport viewport;
    private Camera camera;
    private float timer = MOVE_TIME;
    private int snakeX = 0;
    private int snakeY = 0;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont bitmapFont;
    private GlyphLayout layout = new GlyphLayout();
    private Texture snakeHead;
    private Texture snakeBody;
    private Texture wall;
    private Array<BodyPart> bodyParts = new Array<>();
    private int snakeXBeforeUpdate;
    private int snakeYBeforeUpdate;
    private boolean directionSet = false;
    private State state = State.INITIAL;
    private int score = 0;
    private Array<Wall> walls;
    private boolean isSideControllerNeeded = true;
    private InputHandler inputHandler;

    public GameScreen(SnakeGame game) {
        super(game);
        if (isSideControllerNeeded) {
            controlRenderer = new OnscreenControlRenderer();
        }
    }

    @Override
    public void show() {
        Gdx.graphics.setWindowedMode(WORLD_WIDTH, WORLD_HEIGHT);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        //camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        bitmapFont = new BitmapFont();
        snakeHead = new Texture(Gdx.files.internal("snakehead_resized.png"));
        snakeBody = new Texture(Gdx.files.internal("snakebody.png"));
        appleObj = new Apple(0, 0, new Texture(Gdx.files.internal("apple.png")));
        wall = new Texture(Gdx.files.internal("wall.png"));
        walls = new Array<>();
        for (int i = 4 * GRID_CELL; i < 10 * GRID_CELL; i += GRID_CELL) {
            walls.add(new Wall(wall, i, 10 * GRID_CELL));
        }
        inputHandler = new InputHandler(WORLD_WIDTH, WORLD_HEIGHT);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    @Override
    public void render(float delta) {
        switch (state) {

            case INITIAL:
                checkForInit();
                break;
            case PLAYING:
                queryInput();
                updateSnake(delta);
                checkAppleCollision();
                checkAndPlaceApple();
                break;
            case GAME_OVER:
                checkForInit();
                break;
        }
        clearScreen();
        //    drawGrid();
        draw();
        controlRenderer.render();
    }

    private void updateSnake(float delta) {
        timer -= delta;
        if (timer <= 0) {
            timer = MOVE_TIME;
            moveSnake();
            checkForOutOfBounds();
            updateBodyPartsPosition();
            checkSnakeBodyCollision();
            directionSet = false;
        }
    }

    private void drawGrid() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int x = 0; x < viewport.getWorldWidth(); x += GRID_CELL) {
            for (int y = 0; y < viewport.getWorldHeight(); y += GRID_CELL) {
                shapeRenderer.rect(x, y, GRID_CELL, GRID_CELL);
            }
        }
        shapeRenderer.end();
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        batch.draw(snakeHead, snakeX, snakeY);
        for (BodyPart bodyPart : bodyParts) {
            bodyPart.draw(batch, snakeX, snakeY);
        }
        for (Wall wall : walls) {
            wall.draw(batch);
        }
        if (appleObj.isAppleAvailable) {
            batch.draw(appleObj.texture, appleObj.x, appleObj.y);
        }
        printState(state);
        drawScore();
        batch.end();
    }

    private void printState(State state) {
        switch (state) {
            case INITIAL:
                printToMiddleScreen(GAME_START_TEXT);
                break;
            case GAME_OVER:
                printToMiddleScreen(GAME_OVER_TEXT);
                break;
        }
    }

    private void printToMiddleScreen(String message) {
        layout.setText(bitmapFont, message);
        bitmapFont.draw(batch, message, (viewport.getWorldWidth() -
                layout.width) / 2, (viewport.getWorldHeight() - layout.height) / 2);
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), Color.BLACK.getAlpha());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void checkAppleCollision() {
        if (appleObj.isAppleAvailable && appleObj.x == snakeX && appleObj.y == snakeY) {
            BodyPart bodyPart = new BodyPart(snakeBody);
            bodyPart.updateBodyPosition(snakeX, snakeY);
            bodyParts.insert(0, bodyPart);
            addToScore();
            appleObj.isAppleAvailable = false;
        }
    }

    private void updateBodyPartsPosition() {
        if (bodyParts.size > 0) {
            BodyPart bodyPart = bodyParts.removeIndex(0);
            bodyPart.updateBodyPosition(snakeXBeforeUpdate, snakeYBeforeUpdate);
            bodyParts.add(bodyPart);
        }

    }

    private void checkAndPlaceApple() {
        if (!appleObj.isAppleAvailable) {
            do {
                appleObj.x = MathUtils.random((int) (BOARD_WIDTH)
                        / SNAKE_MOVEMENT - 1) * SNAKE_MOVEMENT + GRID_CELL;

                appleObj.y = MathUtils.random((int) (BOARD_HEIGHT)
                        / SNAKE_MOVEMENT - 1) * SNAKE_MOVEMENT + GRID_CELL;
                appleObj.isAppleAvailable = true;

            } while (appleObj.x == snakeX && appleObj.y == snakeY);
        }
    }

    private void checkForOutOfBounds() {
        if (snakeX >= BOARD_WIDTH + GRID_CELL) {
            snakeX = GRID_CELL;
        }
        if (snakeX < GRID_CELL) {
            snakeX = (int) viewport.getWorldWidth() - GRID_CELL;
        }
        if (snakeY >= BOARD_HEIGHT + GRID_CELL) {
            snakeY = GRID_CELL;
        }
        if (snakeY < GRID_CELL) {
            snakeY = (int) viewport.getWorldHeight() - GRID_CELL;
        }
    }

    private void moveSnake() {
        snakeXBeforeUpdate = snakeX;
        snakeYBeforeUpdate = snakeY;
        switch (snakeDirection) {
            case RIGHT:
                snakeX += SNAKE_MOVEMENT;
                break;
            case LEFT:
                snakeX -= SNAKE_MOVEMENT;
                break;
            case UP:
                snakeY += SNAKE_MOVEMENT;
                break;
            case DOWN:
                snakeY -= SNAKE_MOVEMENT;
                break;
        }
    }

    private void queryInput() {
        boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
        boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S);

        Direction way= inputHandler.handleMouseClick(camera);
        if (way!=Direction.NONE){
            updateDirection(way);
        }
        if (lPressed) updateDirection(Direction.LEFT);
        if (rPressed) updateDirection(Direction.RIGHT);
        if (uPressed) updateDirection(Direction.UP);
        if (dPressed) updateDirection(Direction.DOWN);


    }

    private void updateIfNotOppositeDirection(Direction newSnakeDirection, Direction oppositeDirection) {
        if (snakeDirection != oppositeDirection || bodyParts.size == 0) {
            snakeDirection = newSnakeDirection;
        }
    }

    private void updateDirection(Direction newSnakeDirection) {
        if (!directionSet && snakeDirection != newSnakeDirection) {
            directionSet = true;
            switch (newSnakeDirection) {
                case LEFT:
                    updateIfNotOppositeDirection(newSnakeDirection, Direction.RIGHT);
                    break;
                case RIGHT:
                    updateIfNotOppositeDirection(newSnakeDirection, Direction.LEFT);
                    break;
                case UP:
                    updateIfNotOppositeDirection(newSnakeDirection, Direction.DOWN);
                    break;
                case DOWN:
                    updateIfNotOppositeDirection(newSnakeDirection, Direction.UP);
                    break;
            }
        }
    }

    private void checkSnakeBodyCollision() {
        for (BodyPart bodyPart : bodyParts) {
            if (bodyPart.x == snakeX && bodyPart.y == snakeY) {
                state = State.GAME_OVER;
                return;
            }
        }
        for (Wall wall : walls) {
            if (wall.x == snakeX && wall.y == snakeY) {
                state = State.GAME_OVER;
                return;
            }
        }
    }

    private void checkForInit() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            doInit();
        }
    }

    private void doInit() {
        state = State.PLAYING;
        bodyParts.clear();
        snakeDirection = Direction.RIGHT;
        directionSet = false;
        timer = MOVE_TIME;
        snakeX = 0;
        snakeY = 0;
        snakeXBeforeUpdate = 0;
        snakeYBeforeUpdate = 0;
        appleObj.isAppleAvailable = false;
        score = 0;
    }

    private void addToScore() {
        score += POINTS_PER_APPLE;
    }

    private void drawScore() {
        if (state == State.PLAYING) {
            String scoreAsString = Integer.toString(score);
            layout.setText(bitmapFont, scoreAsString);
            bitmapFont.draw(batch, scoreAsString, (viewport.getWorldWidth() - layout.width) / 2, (4 * viewport.getWorldHeight() / 5) -
                    layout.height / 2);
        }
    }
}
