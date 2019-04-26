package com.allsrc.monstertrucks;

import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public class Ball extends BulletObject {

    public Model model;
    public static float size = 1f;

    public void init() {
        name = "ball";
        attrs = new String[]{ "color", "pos" };
    }

    public Ball(String line) {
        init();
        loadFromLine(line);
        construct();
    }

    public Ball(Vector3 pos) {
        this(MonsterColor.randomColor(), pos);
    }

    public Ball(Color color, Vector3 pos) {
        init();
        setColor(color);
        setPos(pos);
        construct();
    }

    public void construct() {
        entity();
        updateColor();
        updatePos();
    }

    public static void load() {
        Planet.EX.loader.addModel("ball", createSphere());

        btSphereShape meshShape = new btSphereShape(size / 2f);

        BulletConstructor ballConstructor = new BulletConstructor(
            Planet.EX.loader.getModel("ball"), 5f, meshShape);

        ballConstructor.bodyInfo.setRestitution(1f);

        Planet.EX.world.addConstructor("ball", ballConstructor);
    }

    public static Model createSphere() {
        return Planet.EX.modelBuilder.createSphere(size, size, size, 16, 16,
            new Material(new ColorAttribute(ColorAttribute.Diffuse, new Color())),
            Usage.Position | Usage.Normal);
    } 
}