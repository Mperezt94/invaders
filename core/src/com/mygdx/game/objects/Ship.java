package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;
import com.mygdx.game.Controls;

public class Ship {

    enum State {
        IDLE, LEFT, RIGHT, SHOOT, DESTROYED
    }

    Vector2 position;

    State state;
    float stateTime;
    float speed = 5;
    int life;

    TextureRegion frame;

    Weapon weapon;

    Ship(int initialPosition){
        position = new Vector2(initialPosition, 10);
        state = State.IDLE;
        stateTime = 0;
        weapon = new Weapon();
        life = 3;
    }


    void setFrame(Assets assets){
        switch (state){
            case IDLE:
                frame = assets.naveidle.getKeyFrame(stateTime, true);
                break;
            case LEFT:
                frame = assets.naveleft.getKeyFrame(stateTime, true);
                break;
            case RIGHT:
                frame = assets.naveright.getKeyFrame(stateTime, true);
                break;
            case SHOOT:
                frame = assets.naveshoot.getKeyFrame(stateTime, true);
                break;
            case DESTROYED:
                frame = assets.navedestroyed.getKeyFrame(stateTime, false);
                break;
            default:
                frame = assets.naveidle.getKeyFrame(stateTime, true);
                break;
        }
    }

    void render(SpriteBatch batch){
        batch.draw(frame, position.x, position.y);

        weapon.render(batch);
    }

    public void update(float delta, Assets assets) {
        stateTime += delta;


        if (state != State.DESTROYED) {
            if (Controls.isLeftPressed()) {
                moveLeft();
            } else if (Controls.isRightPressed()) {
                moveRight();
            } else {
                idle();
            }

            if (Controls.isShootPressed()) {
                shoot();
                assets.shootSound.play();
            }

            if (life == 0) {
                state = State.DESTROYED;
                stateTime = 0;
            }
            else if(life<0)life = 0;

            weapon.update(delta, assets);
        }
        setFrame(assets);


    }

    void idle(){
        state = State.IDLE;
    }

    void moveLeft(){
        position.x -= speed;
        state = State.LEFT;
    }

    void moveRight(){
        position.x += speed;
        state = State.RIGHT;
    }

    void shoot(){
        state = State.SHOOT;
        weapon.shoot(position.x +16);
    }

    public void damage() {
        if(life>0) {
            life--;
        }
    }

    public void powerUp() {

        weapon.powerUp();
    }

}
