package com.allsrc.monstertrucks;

import com.badlogic.gdx.math.Vector3;

public class ColorChanger extends Trigger {

    public void init() {
        name = "changer";
        attrs = new String[]{ "pos", "rot" };
    }

    public ColorChanger(String line) {
        super();
        init();
        loadFromLine(line);
        construct();
    }

    public ColorChanger(Vector3 pos) {
        super();
        init();
        setPos(pos);
        construct();
    }

    public void construct() {
        entity();
        updatePos();
        randomRot();

        setColor(MonsterColor.randomColor());
        updateColor();
    }

    public static void load() {
        Planet.EX.loader.loadModel("changer", "data/block.obj");
        addDefaultConstructor("changer");
    }

    public void triggered() {
        color = testing.color;
        updateColor();
    }

    public void update() {
        super.update();

        for (BulletObject object : Planet.EX.level.bulletObjects) {
            testing = object;
            testCollision(object.entity.body);
        }
    }
}