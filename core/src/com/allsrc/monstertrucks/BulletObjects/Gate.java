package com.allsrc.monstertrucks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class Gate extends BulletObject {
    public void init() {
        name = "gate";
        attrs = new String[]{ "color", "pos", "rot" };
    }

    public Gate(String line) {
        init();
        loadFromLine(line);
        construct();
    }

    public Gate(Vector3 pos) {
        this(MonsterColor.randomColor(), pos);
    }

    public Gate(Color color, Vector3 pos) {
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
        Planet.EX.loader.loadModel("gate", "data/gate.obj");
        addDefaultConstructor("gate");
    }
}