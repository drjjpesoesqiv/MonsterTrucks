package com.allsrc.monstertrucks;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;

public class ParscheCar extends Car {
    public ParscheCar(Vector3 pos, String color) {
        super(pos, color);

        name = "parsche";
        chassisName = "parsche";
        wheelName = "parsche_wheel";

        wheelScale = new Vector3(1f, 0.75f, 0.75f);

        maxForce = 50f;
        acceleration = 100f; // second

        init();
    }

    public static void load() {
        Planet.EX.loader.loadModel("parsche", "data/cars/parsche.g3dj", true);

        Planet.EX.loader.loadTexture("parsche_red", "data/cars/parsche_red.png", true);
        Planet.EX.loader.loadTexture("parsche_green", "data/cars/parsche_green.png", true);
        Planet.EX.loader.loadTexture("parsche_blue", "data/cars/parsche_blue.png", true);
        Planet.EX.loader.loadTexture("parsche_yellow", "data/cars/parsche_yellow.png", true);

        Planet.EX.loader.loadModel("parsche_wheel", "data/wheel.obj", true);
    }
}