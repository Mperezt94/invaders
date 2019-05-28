package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Assets;
import com.mygdx.game.Controls;
import com.mygdx.game.Timer;

public class World {
    Space space;
    Ship ship;
    AlienArmy alienArmy;
    PowerUp powerUp;
    Timer powerUpTimer;
    float powerUpTime = 15f;
    static state state;

    enum state{RUNING, PAUSE}

    BitmapFont font = new BitmapFont();
    BitmapFont pauseFont = new BitmapFont();

    int WORLD_WIDTH, WORLD_HEIGHT;

    public World(int WORLD_WIDTH, int WORLD_HEIGHT){
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.WORLD_HEIGHT = WORLD_HEIGHT;

        space = new Space();
        ship = new Ship(WORLD_WIDTH/2);
        alienArmy = new AlienArmy(WORLD_WIDTH, WORLD_HEIGHT);
        powerUp = new PowerUp((int)(Math.random()*(WORLD_WIDTH-20)) + 15);
        powerUpTimer = new Timer(powerUpTime);
        state = World.state.RUNING;
    }

    public void render(float delta, SpriteBatch batch, Assets assets){

        update(delta, assets);

        batch.begin();

        space.render(batch);
        ship.render(batch);
        alienArmy.render(batch);
        powerUp.render(batch);
        font.draw(batch,"LIFE: "+ Integer.toString(ship.life),320,25);

        if(state== World.state.PAUSE){

            pauseFont.getData().setScale(2);
            pauseFont.draw(batch,"PAUSE",WORLD_WIDTH/2-45,WORLD_HEIGHT/2-20);
        }

        batch.end();

    }

    void update(float delta, Assets assets){

        if(Controls.isSpacePressed()){
            pause();
        }

        if(state== World.state.RUNING) {

            powerUpTimer.update(delta);

            space.update(delta, assets);
            ship.update(delta, assets);
            alienArmy.update(delta, assets);
            powerUp.update(delta, assets);

            launchPowerUp();
            checkCollisions(assets);
        }

    }

    void launchPowerUp(){
        if (powerUpTimer.check()){
            powerUp.activate();
        }

    }

    private void checkCollisions(Assets assets) {
        checkNaveInWorld();
        checkShootsInWorld();
        checkShootsToAlien(assets);
        checkShootsToShip();
        checkPowerUpCollision();
    }

    private void checkPowerUpCollision() {

        if(powerUp.state == PowerUp.State.ACTIVE) {
            Rectangle shipRectangle = new Rectangle(ship.position.x, ship.position.y, ship.frame.getRegionWidth(), ship.frame.getRegionHeight());
            Rectangle powerUpRectangle = new Rectangle(powerUp.position.x, powerUp.position.y, powerUp.frame.getRegionWidth(), powerUp.frame.getRegionHeight());

            if (Intersector.overlaps(powerUpRectangle, shipRectangle)) {

                ship.powerUp();
                powerUp.remove();

            }
        }

    }

    private void checkShootsToShip() {
        Rectangle shipRectangle = new Rectangle(ship.position.x, ship.position.y, ship.frame.getRegionWidth(), ship.frame.getRegionHeight());

        for(AlienShoot shoot: alienArmy.shoots){
            Rectangle shootRectangle = new Rectangle(shoot.position.x, shoot.position.y, shoot.frame.getRegionWidth(), shoot.frame.getRegionHeight());

            if (Intersector.overlaps(shootRectangle, shipRectangle)) {
                ship.damage();
                shoot.remove();
            }
        }
    }

    private void checkShootsToAlien(Assets assets) {
        for(Shoot shoot: ship.weapon.shoots){
            Rectangle shootRectangle = new Rectangle(shoot.position.x, shoot.position.y, shoot.frame.getRegionWidth(), shoot.frame.getRegionHeight());
            for(Alien alien: alienArmy.aliens){
                if(alien.isAlive()) {
                    Rectangle alienRectangle = new Rectangle(alien.position.x, alien.position.y, alien.frame.getRegionWidth(), alien.frame.getRegionHeight());

                    if (Intersector.overlaps(shootRectangle, alienRectangle)) {
                        alien.kill();
                        shoot.remove();
                        assets.aliendieSound.play();
                    }
                }
            }
        }
    }

    private void checkShootsInWorld() {
        for(Shoot shoot: ship.weapon.shoots){
            if(shoot.position.y > WORLD_HEIGHT){
                shoot.remove();
            }
        }

        for(AlienShoot shoot: alienArmy.shoots){
            if(shoot.position.y < 0){
                shoot.remove();
            }
        }
    }

    private void checkNaveInWorld() {
        if(ship.position.x > WORLD_WIDTH-32){
            ship.position.x = WORLD_WIDTH-32;
        } else if(ship.position.x < 0){
            ship.position.x = 0;
        }
    }

    public boolean gameOver() {

        if(ship.life==0)return true;
        else return false;

    }


    public boolean endGame() {
        if(alienArmy.aliens.size==0)return true;
        else return false;
    }

    private void pause(){
        if(state== World.state.RUNING) state= World.state.PAUSE;
        else state= World.state.RUNING;
    }

}
