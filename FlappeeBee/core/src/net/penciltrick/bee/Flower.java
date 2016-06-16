package net.penciltrick.bee;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Flower {
    private static final float COLLISION_RECTANGLE_WIDTH = 13F;
    private static final float COLLISION_RECTANGLE_HEIGHT = 445F;
    private static final float COLLISION_CIRCLE_RADIUS = 33F;
    private static final float MAX_SPEED_PER_SECOND = 100F;
    public static final float WIDTH = COLLISION_CIRCLE_RADIUS * 2;
    public static final float GAP_BEETWEEN_FLOWERS = 200F;
    private static final float HEIGHT_OFFSET = -400F;

    public float getX() {
        return x;
    }

    private final Circle collisionCircle;
    private final Rectangle collisionRectangle;

    private float x = 0;
    private float y = 0;

    public Flower() {
        this.y = MathUtils.random(HEIGHT_OFFSET);
        this.collisionRectangle = new Rectangle(x, y, COLLISION_RECTANGLE_WIDTH, COLLISION_RECTANGLE_HEIGHT);
        this.collisionCircle = new Circle(x + collisionRectangle.width / 2, y + collisionRectangle.height, COLLISION_CIRCLE_RADIUS);
    }

    public void setPosition(float x) {
        this.x = x;
        updateCollisionCircle();
        updateCollisionRectangle();
    }

    public void updateCollisionCircle() {
        collisionCircle.setX(x + collisionRectangle.width / 2);
        collisionCircle.setY(y + collisionRectangle.height);
    }

    public void updateCollisionRectangle() {
        collisionRectangle.setX(x);
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(collisionCircle.x, collisionCircle.y, collisionCircle.radius);
        shapeRenderer.rect(collisionRectangle.x, collisionRectangle.y, collisionRectangle.width, collisionRectangle.height);
    }

    public void update(float delta) {
        setPosition(x - (MAX_SPEED_PER_SECOND * delta));
    }
}
