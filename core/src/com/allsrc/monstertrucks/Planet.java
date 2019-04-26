package com.allsrc.monstertrucks;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import com.badlogic.gdx.assets.AssetManager;

public enum Planet {
    EX;

    public Settings settings = new Settings();
    public GUI gui = new GUI();

    public MonsterTrucks main;
    public BulletWorld world;
    public Loader loader;
    public Level level;
    public Editor editor;

    public ModelBuilder modelBuilder = new ModelBuilder();
    
    public Array<Disposable> disposables = new Array<Disposable>();
    public Array<Car> cars = new Array<Car>();

    public Race race;

    public void update() {
        world.update();
        level.update();
        gui.update();
    }

    public void dispose() {
        world.dispose();
        level.dispose();
        gui.dispose();

        for (Disposable disposable : disposables)
            disposable.dispose();

        disposables.clear();

        world = null;
    }
}