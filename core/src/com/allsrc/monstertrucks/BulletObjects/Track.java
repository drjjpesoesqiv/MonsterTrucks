package com.allsrc.monstertrucks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;

public class Track extends BulletObject {

    public void init() {
        name = "track";
        attrs = new String[]{ "pos" };
    }

    public Track(String line) {
        init();
        loadFromLine(line);
    }

    public Track() {
        init();
    }

    public void construct(String type) {
        name = type;
        entity(type);
        
        updateTexture();
    }

    public void entity(String type) {
        entity = Planet.EX.world.add(type, 0f, 0f, 0f);
        // addToBulletObjects(this);
    }
    public static void load() {
        Planet.EX.loader.loadModel("straight" , "data/road.g3db", true);
        Planet.EX.loader.loadTexture("straight" , "data/roadbend.png", true);
        addDefaultConstructor("straight");

        // flip UV
        // Matrix3 m3 = new Matrix3();
        // m3.scl(new Vector3(1, 1, 1));
        // Planet.EX.loader.getModel("straight").meshes.get(0).transformUV(m3);

        Planet.EX.loader.loadModel("turn" , "data/roadbend.g3db", true);
        Planet.EX.loader.loadTexture("turn" , "data/roadbend.png", true);
        addDefaultConstructor("turn");
    }
}