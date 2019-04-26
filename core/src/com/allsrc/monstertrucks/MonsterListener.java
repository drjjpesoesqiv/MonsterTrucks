package com.allsrc.monstertrucks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class MonsterListener extends MonsterListenerBase {
    
    private int startedX = 0;
    private int startedY = 0;

    private Car car;
    private boolean isMobile;

    public MonsterListener() {
        car = Planet.EX.cars.get(0);
        isMobile = MonsterUtils.isMobile();
    }

    @Override
    public boolean keyUp (int keycode) {
        switch (keycode) {
             case Keys.P:
                car.pause();
                break;
            case Keys.W:
                car.upPressed = false;
                break;
            case Keys.A:
                car.horzAxis = 0;
                break;
            case Keys.D:
                car.horzAxis = 0;
                break;
        }

        return false;
    }

    @Override
    public boolean keyDown (int keycode) {
        switch (keycode) {
            case Keys.W:
                car.upPressed = true;
                break;
            case Keys.A:
                car.horzAxis = -0.5f;
                break;
            case Keys.D:
                car.horzAxis = 0.5f;
                break;
        }

        return false;
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        if (isMobile) {
            startedX = screenX;
            startedY = screenY;

            if (screenY < 200) {
                if (screenX < Gdx.graphics.getWidth() / 2) {
                    car.reset();
                } else {
                    Planet.EX.editor.deselect();
                    Planet.EX.level.clearLevel();
                    Planet.EX.level.loadFromFile();
                }

                return false;
            }

            if (screenX > Gdx.graphics.getWidth() / 2)
                car.downPressed = true;
            else
                car.upPressed = true;
        }

        return false;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if (isMobile) {

            startedX = 0;
            startedY = 0;

            // car = Planet.EX.cars.get(0);
        
            car.currentAngle = 0f;
            car.upPressed = false;
            car.downPressed = false;
        }

        return false;
    }

    private int x = 0;
    private int y = 0;

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        if (isMobile) {
            // car = Planet.EX.cars.get(0);

            if (startedX != 0) {
                x = (screenX - startedX) * -1;

                // off terrain rotation
                car.horzAxis = MonsterUtils.map(x, -300, 300, 1, -1);

                if (x > 200) x = 200;
                if (x < -200) x = -200;

                // steering
                car.currentAngle = MonsterUtils.map(x, -200f, 200f,
                    -car.maxAngle,
                    car.maxAngle);
            }

            // covers off terrain rotation
            if (startedY != 0) {
                y = (screenY - startedY) * -1;
                car.vertAxis = MonsterUtils.map(y, -300, 300, 1, -1);
            }
        }

        return false;
    }
}