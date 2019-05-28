package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Controls;
import com.mygdx.game.SpaceInvaders;
import com.mygdx.game.objects.Space;

public class WinScreen extends SpaceInvadersScreen {

    public SpriteBatch spriteBatch;

    public OrthographicCamera camera;
    public Viewport viewport;

    public int SCENE_WIDTH = 384;
    public int SCENE_HEIGHT = 256;

    Space space;
    BitmapFont font = new BitmapFont();


    public WinScreen(SpaceInvaders spaceInvaders) {
        super(spaceInvaders);
    }


    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(SCENE_WIDTH/2, SCENE_HEIGHT/2, 0);
        viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
        viewport.apply();

        spriteBatch = new SpriteBatch();
        space = new Space();

    }

    public void update(float delta) {

        space.update(delta,assets);

        if(Controls.isEnterPressed()){
            Gdx.app.exit();
        }

    }

    @Override
    public void render(float delta) {

        update(delta);

        spriteBatch.begin();
        spriteBatch.setProjectionMatrix(camera.combined);
        space.render(spriteBatch);
        font.getData().setScale(2);
        font.draw(spriteBatch,"YOU WON",SCENE_WIDTH/2-70,SCENE_HEIGHT/2+20);
        font.getData().setScale(1);
        font.draw(spriteBatch,"Press ENTER to close",SCENE_WIDTH/2-65,SCENE_HEIGHT/2-20);
        spriteBatch.end();


    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);
        viewport.update(width ,height);
    }

}
