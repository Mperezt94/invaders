package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;

public class PowerUp {

    enum State {
        ACTIVE, INACTIVE
    }

    State state;
    Vector2 position;
    float stateTime;
    TextureRegion frame;
    float speed = 1f;
    int y = 350;


    public PowerUp(int x){
        state = State.INACTIVE;
        position = new Vector2(x, y);
        stateTime = 0f;
    }

    public void render(SpriteBatch batch) {
        if(state == State.ACTIVE) {
            batch.draw(frame, position.x, position.y);
        }
    }

    public void update(float delta, Assets assets) {

        stateTime += delta;
        frame = assets.powerup.getKeyFrame(stateTime, true);

        if (state == State.ACTIVE) {
            move();
        }

    }

    public void move(){
        position.y -= speed;
    }

    public void remove() {
        state = State.INACTIVE;
        position.y = y;

    }

    public void activate(){
        state = State.ACTIVE;
    }
}
