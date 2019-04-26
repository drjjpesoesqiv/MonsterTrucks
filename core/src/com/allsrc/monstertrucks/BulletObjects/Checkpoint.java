package com.allsrc.monstertrucks;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

public class Checkpoint extends Trigger {
    public int id;
    public Gate gate;
    public static float size = 10f;
    protected Color activeColor;
    protected static Color inactiveColor = new Color(0.5f, 0.5f, 0.5f, 1f);

    int testing;

    public void init() {
        name = "checkpoint";
        attrs = new String[]{ "color", "pos", "rot", "size" };
    }

    public Checkpoint(String line) {
        super();
        init();
        loadFromLine(line);
        construct();
    }

    public Checkpoint(Vector3 pos) {
        this(MonsterColor.randomColor(), pos, new Vector3(10, 10, 10));
    }

    public Checkpoint(Color color, Vector3 pos, Vector3 size) {
        super();
        init();
        setColor(color);
        setPos(pos);
        setSize(size);
        construct();
    }

    public void construct() {
        entity();
        // addGate();
        adjustColor();
        updateColor();
        updateRot();
        noResponse();

        activeColor = getColor();
    }

    public void setPos(Vector3 pos) {
        super.setPos(pos);

        if (gate != null) {
            gate.setPos(getPos());
            gate.updatePos();
        }
    }

    public void updateRot() {
        // gate.setRot(getRot());
        // gate.updateRot();
        // gate.updatePos();

        super.updateRot();
        super.updatePos();
    }

    public void adjustColor() {
        color.a = 0f;
        updateColor();
    }

    public void addGate() {
        gate = new Gate(getColor(), getPos());
        gate.updateColor();
        gate.updatePos();
        BulletObject.removeFromBulletObjects(gate);
    }

    public void dispose() {
        // gate.dispose();
        super.dispose();
    }

    public void triggered() {
        Planet.EX.level.mode.reachedCheckpoint(id, testing);
    }

    public void update() {
        int i = 0;
        for (Car car : Planet.EX.cars) {
            testing = i;
            testCollision(car.entity.body);
            i++;
        }
    }

    public static void load() {
        // Gate.load();

        Planet.EX.loader.addModel("checkpoint", createSphere());

        addDefaultConstructor("checkpoint");
    }

    public static Model createSphere() {
        return Planet.EX.modelBuilder.createSphere(size, size, size, 16, 16,
            new Material(new ColorAttribute(ColorAttribute.Diffuse, Color.RED)),
            Usage.Position | Usage.Normal);
    }

    public void setInactive() {
        pause();
        color = inactiveColor;
        updateColor();

        // gate.setColor(inactiveColor);
        // gate.updateColor();
    }

    public void setActive() {
        start();
        color = activeColor;
        updateColor();

        // gate.setColor(activeColor);
        // gate.updateColor();
    }

    public String getSaveColor() {
        return df.format(activeColor.r) + "," + df.format(activeColor.g) + "," + df.format(activeColor.b) + "," + df.format(activeColor.a);
    }

    public void render() {
        // super.render();
        // gate.render();
    }
}