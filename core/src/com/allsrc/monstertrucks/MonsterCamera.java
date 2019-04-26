package com.allsrc.monstertrucks;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.PerspectiveCamera;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class MonsterCamera {
    private PerspectiveCamera camera;

    // context of truck
    private Matrix4 worldTransform = new Matrix4();
    private Vector3 cameraPosition = new Vector3();
    private Vector3 objPosition = new Vector3();

    private Vector3 tempV1 = new Vector3(0,0,0);
    private Vector3 tempV2 = new Vector3(0,0,0);

    private Vector3 camOffset = new Vector3(-4.5f, 13, -6.5f);
    private Vector3 editorCamOffset = new Vector3(-4.5f, 20, -6.5f);

    public MonsterCamera(int playerCount) {
        float width = 0;

        switch (Planet.EX.settings.playerCount) {
            case 1:
                width = 3f * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
                break;
            case 2:
                width = 3f * Gdx.graphics.getWidth() / (Gdx.graphics.getHeight() / 2);
                break;
            case 3:
            case 4:
                break;
        }

        camera = new PerspectiveCamera(63f, width, 3f);
    }

    public PerspectiveCamera get() {
        return this.camera;
    }

    public void focus(BulletObject obj) {
        obj.entity.motionState.getWorldTransform(worldTransform);
        worldTransform.getTranslation(objPosition);

        if (!Planet.EX.editor.active)
            tempV1.set(camOffset);
        else
            tempV1.set(editorCamOffset);

        cameraPosition.set(objPosition.x + tempV1.x, tempV1.y, objPosition.z + tempV1.z);

        camera.position.set(cameraPosition);
        camera.lookAt(objPosition);
        camera.up.set(Vector3.Y);
        camera.update();
    }

    public boolean isVisible(BulletObject obj) {
        obj.entity.modelInstance.transform.getTranslation(tempV2);
        tempV2.add(Planet.EX.loader.getCenter(obj.name));

        return camera.frustum.sphereInFrustum(tempV2, Planet.EX.loader.getRadius(obj.name));
    }
}