package com.allsrc.monstertrucks;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.AllHitsRayResultCallback;

import com.badlogic.gdx.Input.Keys;

public class EditorListener extends MonsterListenerBase {
    ClosestRayResultCallback rayTestCB = new ClosestRayResultCallback(Vector3.Zero, Vector3.Z);
    AllHitsRayResultCallback allTestCB = new AllHitsRayResultCallback(Vector3.Zero, Vector3.Z);
    Vector3 rayFrom = new Vector3();
    Vector3 rayTo = new Vector3();

    @Override
    public boolean keyUp (int keycode) {
        if (keycode >= 8 && keycode <= 14) {
            Planet.EX.editor.setActiveObject(keycode - 8);

            return false;
        }

        switch (keycode) {
            case Keys.E:
                Planet.EX.editor.setActive(!Planet.EX.editor.active);
                break;
            case Keys.C:
                Planet.EX.editor.deselect();
                Planet.EX.level.clearLevel();
                break;
            case Keys.L:
                Planet.EX.editor.deselect();
                Planet.EX.level.saveToFile();
                break;
            case Keys.O:
                Planet.EX.editor.deselect();
                Planet.EX.level.loadFromFile();
                break;

            // height
            case Keys.NUMPAD_1:
                Planet.EX.editor.moveY(-0.5f);
                break;
            case Keys.NUMPAD_3:
                Planet.EX.editor.moveY(0.5f);
                break;
        }

        Planet.EX.editor.keyUp(keycode);

        return false;
    }

    @Override
    public boolean scrolled (int amount) {
        if (!Planet.EX.editor.active)
            return false;

        Planet.EX.editor.scroll(amount);
        return false;
    }

    @Override
    public boolean mouseMoved (int screenX, int screenY) {
        if (!Planet.EX.editor.active)
            return false;

        if (Planet.EX.settings.playerCount == 2)
            screenY = splitScreenCorrection(screenY);

        Ray ray = Planet.EX.main.camera.get().getPickRay(screenX, screenY);
        rayFrom.set(ray.origin);
        rayTo.set(ray.direction).scl(50f).add(rayFrom);

        allTestCB.setCollisionObject(null);
        allTestCB.setClosestHitFraction(4f);
        allTestCB.setRayFromWorld(rayFrom);
        allTestCB.setRayToWorld(rayTo);

        Planet.EX.world.collisionWorld.rayTest(rayFrom, rayTo, allTestCB);

        if (allTestCB.hasHit()) {
            int i = 0;
            for (int j = 0; j < allTestCB.getCollisionObjects().size(); j++) {
                if (allTestCB.getCollisionObjects().at(j) == Planet.EX.level.terrain.entity.body)
                {
                    Vector3 p = new Vector3(0,0,0);
                    p = allTestCB.getHitPointWorld().at(i);
                    Planet.EX.editor.mouseMoved(p);
                }
                i++;
            }
        }

        return false;
    }

    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        if (!Planet.EX.editor.active)
            return false;

        if (Planet.EX.settings.playerCount == 2)
            screenY = splitScreenCorrection(screenY);

        Ray ray = Planet.EX.main.camera.get().getPickRay(screenX, screenY);
        rayFrom.set(ray.origin);
        rayTo.set(ray.direction).scl(50f).add(rayFrom);

        rayTestCB.setCollisionObject(null);
        rayTestCB.setClosestHitFraction(1f);
        rayTestCB.setRayFromWorld(rayFrom);
        rayTestCB.setRayToWorld(rayTo);

        Planet.EX.world.collisionWorld.rayTest(rayFrom, rayTo, rayTestCB);

        if (rayTestCB.hasHit()) {
            Vector3 p = new Vector3(0,0,0);
            rayTestCB.getHitPointWorld(p);

            if (button == 0)
                Planet.EX.editor.leftClick(rayTestCB.getCollisionObject(), new Vector3(p.x, p.y, p.z));
            else
                Planet.EX.editor.rightClick(rayTestCB.getCollisionObject());
        }

        return false;
    }

    private int splitScreenCorrection(float screenY) {
        if (screenY < Gdx.graphics.getHeight() / 2) {
            Planet.EX.main.camera.focus(Planet.EX.cars.get(0));
            screenY = MonsterUtils.map(screenY, 0, Gdx.graphics.getHeight() / 2, 0, Gdx.graphics.getHeight());
        } else {
            Planet.EX.main.camera.focus(Planet.EX.cars.get(1));
            screenY = MonsterUtils.map(screenY, Gdx.graphics.getHeight() / 2, Gdx.graphics.getHeight(), 0, Gdx.graphics.getHeight());
        }

        return Math.round(screenY);
    }
}