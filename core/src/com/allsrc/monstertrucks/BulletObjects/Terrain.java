package com.allsrc.monstertrucks;

import com.badlogic.gdx.graphics.Color;

public class Terrain extends BulletObject {

    public void init() {
        name = "terrain";
        attrs = new String[]{ "color" };
    }

    public Terrain(String line) {
        init();
        loadFromLine(line);
        construct();
    }

    public Terrain(Color color) {
        init();
        setColor(color);
        construct();
    }

    public void construct() {
        entity();
        updateColor();
        removeFromBulletObjects(this);
    }

    public static void load(String modelFile) {
        Planet.EX.loader.loadModel("terrain", modelFile);
        Planet.EX.loader.getModel("terrain").meshes.get(0).scale(5, 5, 5);
        addDefaultConstructor("terrain");
    }
}