package com.allsrc.monstertrucks;

import com.badlogic.gdx.math.Vector3;

public class Coin extends Collectible {

    public void init() {
        name = "coin";
        attrs = new String[]{ "pos" };
    }

    public Coin(String line) {
        super();
        init();
        loadFromLine(line);
        construct();
    }

    public Coin(Vector3 pos) {
        super();
        init();
        setPos(pos);
        construct();
    }

    public void construct() {
        entity();
        noResponse();
        randomRot();
        updateTexture();
    }

    public void update() {
        entity.transform.rotate(Vector3.Y, 1);
        super.update();
    }

    public static void load() {
        Planet.EX.loader.loadModel("coin", "data/coin.obj");
        Planet.EX.loader.loadSound("coin", "data/coins.wav");
        Planet.EX.loader.loadTexture("coin", "data/coin.png");
        addDefaultConstructor("coin");
    }
}