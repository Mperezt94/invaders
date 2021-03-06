package com.mygdx.game.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.SpaceInvaders;
import com.mygdx.game.Timer;
import com.mygdx.game.objects.World;

public class GameScreen extends SpaceInvadersScreen {

    public SpriteBatch spriteBatch;

    public OrthographicCamera camera;
    public Viewport viewport;

    public int SCENE_WIDTH = 384;
    public int SCENE_HEIGHT = 256;

    float waitTime;

    World world;
    Timer timer;

    public GameScreen(SpaceInvaders spaceInvaders) {
        super(spaceInvaders);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(SCENE_WIDTH/2, SCENE_HEIGHT/2, 0);
        viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
        viewport.apply();

        spriteBatch = new SpriteBatch();

        world = new World(SCENE_WIDTH, SCENE_HEIGHT);

        waitTime = 2.5f;

        timer = new Timer(waitTime);
    }

    @Override
    public void render(float delta) {
        spriteBatch.setProjectionMatrix(camera.combined);

        world.render(delta, spriteBatch, assets);

        if(world.gameOver()){
            timer.update(delta);
            if (timer.check()) {
                setScreen(new EndGameScreen(game));
            }
        }
        else if(world.endGame()){
            setScreen(new WinScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);

        viewport.update(width ,height);
    }
}
