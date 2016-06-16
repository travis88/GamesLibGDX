package com.penciltrick.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.penciltrick.game.FlappyDemo;
import com.penciltrick.game.sprites.Bird;
import com.penciltrick.game.sprites.Tube;

public class PlayState extends State {
    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -50;

    private Bird bird;
    private Texture bg;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private Array<Tube> tubes;
    private Music music;
    private static int score = 0;

    private BitmapFont mBitmapFont;
    private GlyphLayout mLayout = new GlyphLayout();

    protected PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(50, 300);
        cam.setToOrtho(false, FlappyDemo.WIDTH / 2, FlappyDemo.HEIGHT / 2);
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + ground.getWidth(), GROUND_Y_OFFSET);
        tubes = new Array<Tube>();
//        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("dreaming.mp3"));
        music.setLooping(true);
        music.setVolume(0.4f);
        music.play();
        mBitmapFont = new BitmapFont();

        for (int i = 1; i <= TUBE_COUNT; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            bird.jump();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        updateGround();
        bird.update(dt);
        cam.position.x = bird.getPosition().x + 80;
        for (int i = 0; i < tubes.size; i++) {
            if (cam.position.x - (cam.viewportWidth / 2) > tubes.get(i).getPosTopTube().x + tubes.get(i).getTopTube().getWidth()) {
                tubes.get(i).reposition(tubes.get(i).getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
                score++;
            }

            if (tubes.get(i).collide(bird.getBounds())) {
                gsm.set(new GameOverState(gsm));
                score = 0;
            }

        }
        if (bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET) {
            gsm.set(new GameOverState(gsm));
            score = 0;
        }

        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        sb.draw(bird.getBird(), bird.getPosition().x, bird.getPosition().y);
        for (int i = 0; i < tubes.size; i++) {
            sb.draw(tubes.get(i).getTopTube(), tubes.get(i).getPosTopTube().x, tubes.get(i).getPosTopTube().y);
            sb.draw(tubes.get(i).getBottomTube(), tubes.get(i).getPosBotTube().x, tubes.get(i).getPosBotTube().y);
        }
        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);
        String scoreString = Integer.toString(score);
        mLayout.setText(mBitmapFont, scoreString);
        mBitmapFont.draw(sb, scoreString, bird.getPosition().x + 80, (int) (cam.viewportHeight * 0.9)  - mLayout.height);
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();
        ground.dispose();
        for (Tube tube : tubes)
            tube.dispose();
        music.dispose();
        System.out.println("Play State Disposed");
    }

    public void updateGround() {
        if ((cam.position.x - cam.viewportWidth / 2) > groundPos1.x + ground.getWidth()) {
            groundPos1.add(ground.getWidth() * 2, 0);
        }
        if ((cam.position.x - cam.viewportWidth / 2) > groundPos2.x + ground.getWidth()) {
            groundPos2.add(ground.getWidth() * 2, 0);
        }
    }
}
