package com.allsrc.monstertrucks;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

public class Level {
    public Array<BulletObject> bulletObjects = new Array<BulletObject>();
    public Array<Collectible> collectibles = new Array<Collectible>();
    public Array<Trigger> triggers = new Array<Trigger>();
    
    public Texture background;

    public Terrain terrain;

    public Environment environment;
    public DirectionalLight light;

    public LevelMode mode;

    public Level() {
        load();
    }

    public void load() {
        Ball.load();
        Checkpoint.load();
        ColorChanger.load();
        Coin.load();
        Gate.load();
        SmallRamp.load();
        LargeRamp.load();
        Track.load();
    }

    public void init() {
        Terrain.load("data/terrain.obj");
        terrain = new Terrain(Color.GREEN);

        background = new Texture(Gdx.files.internal("data/tiles.png"));
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    public void update() {
        mode.update();

        for (Collectible collectible : Planet.EX.level.collectibles)
            collectible.update();

        for (Trigger trigger : Planet.EX.level.triggers)
            trigger.update();
    }

    public void dispose() {
        Planet.EX.level.environment = null;
        Planet.EX.level.light = null;
    }

    public void render() {
        // modelBatch.render(Planet.EX.level.terrain.entity.modelInstance, Planet.EX.level.environment);

        for (BulletObject obj : bulletObjects)
            obj.render();

        mode.render();
    }

    public void clearLevel() {
        collectibles = new Array<Collectible>();
        triggers = new Array<Trigger>();

        for (int j = bulletObjects.size - 1; j >= 0; j--)
            bulletObjects.get(j).dispose();

        bulletObjects = new Array<BulletObject>();
    }

    public void saveToFile() {
        try {
            File file = new File("data/output.txt");  
            FileWriter writer = new FileWriter(file);  
            PrintWriter out = new PrintWriter(writer);

            for (BulletObject bulletObject : bulletObjects) {
                out.println(bulletObject.getSaveLine());
            }

            out.close();
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }

    public void loadFromFile() {
        FileHandle file = Gdx.files.internal("data/output.txt");
        String[] lines = file.readString().split("\n");

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.25f, 0.25f, 0.25f, 1f));
        
        light = new DirectionalLight();
        light.set(1f, 1f, 1f,0.65f, -0.9f,0);

        environment.add(light);

        BulletObject obj = null;
        for (String line : lines)
            obj = Planet.EX.editor.createObject(line);
    }
}