package com.penciltrick.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.penciltrick.game.FlappyDemo;

public class Bird {
    private static final int GRAVITY = -15;
    private static final int MOVEMENT = 100;
    private Vector2 position;
    private Vector2 velocity;
    private Rectangle bounds;
    private Animation birdAnimation;
    private Texture texture;
    private Sound flap;

    private Texture bird;

    public Bird(int x, int y) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        bird = new Texture("bird.png");
        texture = new Texture("birdanimation.png");
        birdAnimation = new Animation(new TextureRegion(texture), 3, 0.5f);
        bounds = new Rectangle(x, y, texture.getWidth() / 3, texture.getHeight());
        flap = Gdx.audio.newSound(Gdx.files.internal("sfx_wing.ogg"));
    }

    public void update(float dt) {
        birdAnimation.update(dt);
        if (position.y > 0) {
            velocity.add(0, GRAVITY);
        }
        velocity.scl(dt);
        position.add(MOVEMENT * dt, velocity.y);
        if (position.y < 0) { position.y = 0; }
        if (position.y > FlappyDemo.HEIGHT / 2 - bird.getHeight()) {
            position.y = FlappyDemo.HEIGHT / 2 - bird.getHeight();
        }

        velocity.scl(1 / dt);
        bounds.setPosition(position.x, position.y);
    }

    public Vector2 getPosition() { return position; }

    public TextureRegion getBird() { return birdAnimation.getFrame(); }

    public void jump() {
        velocity.y = 250;
        flap.play(0.3f);
    }

    public Rectangle getBounds() { return bounds; }

    public void dispose() {
        texture.dispose();
        flap.dispose();
    }
}
