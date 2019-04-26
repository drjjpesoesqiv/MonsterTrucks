package com.allsrc.monstertrucks;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.ContactResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;

import com.badlogic.gdx.audio.Sound;

public class Collectible extends BulletObject {

    public class CollectibleCallback extends ContactResultCallback {
        
        @Override
        public float addSingleResult (btManifoldPoint cp,
            btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
            btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
                if (touched)
                    return 0;

                pickedUp();
                return 0;
        }
    }

    public CollectibleCallback collectibleCallback;;

    public boolean touched = false;

    public Collectible() {
        collectibleCallback = new CollectibleCallback();
    }

    public void entity() {
        super.entity();
        addToCollectibles();
    }

    public void addToCollectibles() {
        Planet.EX.level.collectibles.add(this);
    }

    public void removeFromCollectibles() {
        Planet.EX.level.collectibles.removeValue(this, true);
    }

    public void update() {
        if (!touched)
        {
            for (Car car : Planet.EX.cars) {
                if (entity.body != null)
                    Planet.EX.world.collisionWorld.contactPairTest(car.entity.body, entity.body, collectibleCallback);
            }
        }
    }

    public void pickedUp() {
        Planet.EX.loader.getSound(name).play();

        touched = true;
        dispose();
    }

    public void dispose () {
        Planet.EX.world.remove(entity);
        Planet.EX.world.collisionWorld.removeCollisionObject(entity.body);

        removeFromCollectibles();
        removeFromBulletObjects(this);
        
        entity.dispose();
    }
}