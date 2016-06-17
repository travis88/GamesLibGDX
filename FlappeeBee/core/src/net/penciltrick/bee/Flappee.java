package net.penciltrick.bee;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public class Flappee {
    public static final float COLLISION_RADIUS = 24F;
    private final Circle collisionCircle;
    private static final float DIVE_ACCEL = 0.30F;
    private static final float FLY_ACCEL = 5F;
    private static final float FRAME_DURATION = 0.25F;

    private float x = 0;
    private float y = 0;
    private float ySpeed = 0;

    private static final int TILE_WIDTH = 118;
    private static final int TILE_HEIGHT = 118;
    private final Animation animation;
    private float animationTimer = 0;

    public Flappee(Texture flappeeTexture) {
        TextureRegion[][] flappeeTextures = new TextureRegion(flappeeTexture).split(TILE_WIDTH, TILE_HEIGHT);
        animation = new Animation(FRAME_DURATION, flappeeTextures[0][0], flappeeTextures[0][1]);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        collisionCircle = new Circle(x, y, COLLISION_RADIUS);
    }

    public void setySpeed(float ySpeed) { this.ySpeed = ySpeed; }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Circle getCollisionCircle() { return collisionCircle; }

    public void draw(SpriteBatch batch) {
        TextureRegion flappeeTexture = animation.getKeyFrame(animationTimer) ;
        float textureX = collisionCircle.x - flappeeTexture.getRegionWidth() / 2;
        float textureY = collisionCircle.y - flappeeTexture.getRegionHeight() / 2;
        batch.draw(flappeeTexture, textureX, textureY);
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(collisionCircle.x, collisionCircle.y, collisionCircle.radius);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollisionCircle();
    }

    public void updateCollisionCircle() {
        collisionCircle.setX(x);
        collisionCircle.setY(y);
    }

    public void update(float delta) {
        animationTimer += delta;
        ySpeed -= DIVE_ACCEL;
        setPosition(x, y + ySpeed);
    }

    public void flyUp() {
        ySpeed += FLY_ACCEL;
        setPosition(x, y + ySpeed);
    }
}
