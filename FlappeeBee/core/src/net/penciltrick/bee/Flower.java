package net.penciltrick.bee;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
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
    private static final float DISTANCE_BETWEEN_FLOOR_AND_CEILING = 225F;

    private final Circle floorCollisionCircle;
    private final Rectangle floorCollisionRectangle;
    private final Circle ceilingCollisionCircle;
    private final Rectangle ceilingCollisionRectangle;

    private float x = 0;
    private float y = 0;
    private boolean pointClaimed = false;

    public float getX() {
        return x;
    }

    public Flower() {
        this.y = MathUtils.random(HEIGHT_OFFSET);
        this.floorCollisionRectangle = new Rectangle(x, y, COLLISION_RECTANGLE_WIDTH, COLLISION_RECTANGLE_HEIGHT);
        this.floorCollisionCircle = new Circle(x + floorCollisionRectangle.width, y + floorCollisionRectangle.height, COLLISION_CIRCLE_RADIUS);

        this.ceilingCollisionRectangle = new Rectangle(x, floorCollisionCircle.y + DISTANCE_BETWEEN_FLOOR_AND_CEILING, COLLISION_RECTANGLE_WIDTH, COLLISION_RECTANGLE_HEIGHT);
        this.ceilingCollisionCircle = new Circle(x + ceilingCollisionRectangle.width / 2, ceilingCollisionRectangle.y, COLLISION_CIRCLE_RADIUS);
    }

    public void setPosition(float x) {
        this.x = x;
        updateCollisionCircle();
        updateCollisionRectangle();
    }

    public void updateCollisionCircle() {
        floorCollisionCircle.setX(x + floorCollisionRectangle.width / 2);
        ceilingCollisionCircle.setX(x + ceilingCollisionRectangle.width / 2);
    }

    public void updateCollisionRectangle() {
        floorCollisionRectangle.setX(x);
        ceilingCollisionRectangle.setX(x);
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(floorCollisionCircle.x, floorCollisionCircle.y, floorCollisionCircle.radius);
        shapeRenderer.rect(floorCollisionRectangle.x, floorCollisionRectangle.y, floorCollisionRectangle.width, floorCollisionRectangle.height);
        shapeRenderer.circle(ceilingCollisionCircle.x, ceilingCollisionCircle.y, ceilingCollisionCircle.radius);
        shapeRenderer.rect(ceilingCollisionRectangle.x, ceilingCollisionRectangle.y, ceilingCollisionRectangle.width, ceilingCollisionRectangle.height);
    }

    public void update(float delta) {
        setPosition(x - (MAX_SPEED_PER_SECOND * delta));
    }

    public boolean isFlappeeColliding(Flappee flappee) {
        Circle flappeeCollisionCircle = flappee.getCollisionCircle();
        return
                Intersector.overlaps(flappeeCollisionCircle, ceilingCollisionCircle) ||
                Intersector.overlaps(flappeeCollisionCircle, floorCollisionCircle) ||
                Intersector.overlaps(flappeeCollisionCircle, ceilingCollisionRectangle) ||
                Intersector.overlaps(flappeeCollisionCircle, floorCollisionRectangle);
    }

    public boolean isPointClaimed() { return pointClaimed; }

    public void markPointClaimed() { pointClaimed =true; }
}
