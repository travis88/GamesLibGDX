package net.penciltrick.bee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter {
    public static final int WORLD_WIDTH = 480;
    public static final int WORLD_HEIGHT = 640;
    private ShapeRenderer mShapeRenderer;
    private Viewport mViewport;
    private Camera mCamera;
    private SpriteBatch mBatch;
    private Flappee flappee;
    private Array<Flower> flowers = new Array<Flower>();
    private int score = 0;
    private BitmapFont mBitmapFont;
    private GlyphLayout mGlyphLayout = new GlyphLayout();

    @Override
    public void show() {
        super.show();
        mCamera = new OrthographicCamera();
        mCamera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        mCamera.update();
        mViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        mShapeRenderer = new ShapeRenderer();
        mBatch = new SpriteBatch();
        flappee = new Flappee();
        flappee.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
        mBitmapFont = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        clearScreen();
        update(delta);
        draw();
    }

    public void draw() {
        mBatch.setProjectionMatrix(mCamera.projection);
        mBatch.setTransformMatrix(mCamera.view);
        mBatch.begin();
        drawScore();
        mBatch.end();
    }

    private void drawScore() {
        String scoreAsString = Integer.toString(score);
        mGlyphLayout.setText(mBitmapFont, scoreAsString);
        mBitmapFont.draw(mBatch, mGlyphLayout, (mViewport.getWorldWidth() - mGlyphLayout.width) / 2, (4 * mViewport.getWorldHeight() / 5) - mGlyphLayout.height / 2);
    }

    @Override
    public void resize(int width, int height) {
        mViewport.update(width, height);
    }

    public void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void blockFlappeeLeavingTheWorld() {
        flappee.setPosition(flappee.getX(), MathUtils.clamp(flappee.getY(), flappee.COLLISION_RADIUS, WORLD_HEIGHT - flappee.COLLISION_RADIUS));
    }

    public void update(float delta) {
        updateFlappee();
        updateFlowers(delta);
        if (checkForCollision()) { restart(); }
        mShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        flappee.drawDebug(mShapeRenderer);
        for (Flower flower : flowers) {
            flower.drawDebug(mShapeRenderer);
        }
        mShapeRenderer.end();
        updateScore();
    }

    public void createNewFlower() {
        Flower flower = new Flower();
        flower.setPosition(WORLD_WIDTH + Flower.WIDTH);
        flowers.add(flower);
    }

    private void checkIfNewFlowerIsNeeded() {
        if (flowers.size == 0) {
            createNewFlower();
        } else {
            Flower flower = flowers.peek();
            if (flower.getX() < WORLD_WIDTH - flower.GAP_BEETWEEN_FLOWERS) {
                createNewFlower();
            }
        }
    }

    private void removeFlowersIfPassed() {
        if (flowers.size > 0) {
            Flower flower = flowers.first();
            if (flower.getX() < -Flower.WIDTH) {
                flowers.removeValue(flower, true);
            }
        }
    }

    private void updateFlowers(float delta) {
        for (Flower flower : flowers) {
            flower.update(delta);
        }
        checkIfNewFlowerIsNeeded();
        removeFlowersIfPassed();
    }

    private boolean checkForCollision() {
        for (Flower flower : flowers) {
            if (flower.isFlappeeColliding(flappee)) { return true; }
        }
        return false;
    }

    private void restart() {
        flappee.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
        flowers.clear();
        score = 0;
    }

    private void updateScore() {
        for (Flower flower : flowers) {
            if (flower.getX() < flappee.getX() && !flower.isPointClaimed()) {
                flower.markPointClaimed();
                score++;
                System.out.println(score);
            }
        }
    }

    private void updateFlappee() {
        flappee.update();
        if (Gdx.input.justTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            flappee.flyUp();
            blockFlappeeLeavingTheWorld();
        }
    }
}
