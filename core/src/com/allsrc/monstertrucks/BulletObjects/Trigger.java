package com.allsrc.monstertrucks;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.ContactResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;

public class Trigger extends BulletObject {

    public class TriggerCallback extends ContactResultCallback {
        
        @Override
        public float addSingleResult (btManifoldPoint cp,
            btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
            btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
                if (paused)
                    return 0f;

                triggered();
                return 0f;
        }
    }

    public TriggerCallback triggerCallback;
    public BulletObject testing;

    public boolean paused = false;

    public Trigger() {
        triggerCallback = new TriggerCallback();
        Planet.EX.level.triggers.add(this);
    }

    public void update() {
        for (Car car : Planet.EX.cars) {
            testing = (BulletObject)car;
            testCollision(car.entity.body);
        }
    }

    public void testCollision(btCollisionObject body) {
        if (paused)
            return;
        
        Planet.EX.world.collisionWorld.contactPairTest(body, entity.body, triggerCallback);
    }

    public void dispose() {
        super.dispose();
        Planet.EX.level.triggers.removeValue(this, true);
    }

    public void triggered() {
        //
    }

    public void start() {
        paused = false;
    }

    public void pause() {
        paused = true;
    }

    public void reset() {
        paused = false;
    }
}