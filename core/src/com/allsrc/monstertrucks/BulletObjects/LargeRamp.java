package com.allsrc.monstertrucks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class LargeRamp extends BulletObject {
    public void init() {
        name = "largeramp";
        attrs = new String[]{ "pos", "color", "rot" };
    }

    public LargeRamp(String line) {
        init();
        loadFromLine(line);
        construct();
    }

    public LargeRamp(Vector3 pos) {
        this(MonsterColor.randomColor(), pos);
    }

    public LargeRamp(Color color, Vector3 pos) {
        init();
        setColor(color);
        setPos(pos);
        construct();
    }

    public void construct() {
        entity();
        updateColor();
        
        updateRot();
        updatePos();
    }

    public static void load() {
        Planet.EX.loader.loadModel("largeramp", "data/largeramp.obj");
        addDefaultConstructor("largeramp");
    }
}