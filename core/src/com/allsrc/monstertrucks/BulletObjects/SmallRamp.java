package com.allsrc.monstertrucks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class SmallRamp extends BulletObject {
    public void init() {
        name = "smallramp";
        attrs = new String[]{ "pos", "color", "rot" };
    }

    public SmallRamp(String line) {
        init();
        loadFromLine(line);
        construct();
    }

    public SmallRamp(Vector3 pos) {
        this(MonsterColor.randomColor(), pos);
    }

    public SmallRamp(Color color, Vector3 pos) {
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
        Planet.EX.loader.loadModel("smallramp", "data/smallramp.obj");
        addDefaultConstructor("smallramp");
    }
}