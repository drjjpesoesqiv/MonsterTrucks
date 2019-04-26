package com.allsrc.monstertrucks;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GUI {
    public Skin skin;
    public Stage stage;

    public Label fpsLabel;
    public Label activeObjectLabel;

    public GUI() {
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        fpsLabel = new Label("FPS", skin);
        activeObjectLabel = new Label("Object", skin);

        Table table = new Table();
        table.left().bottom();
        table.setFillParent(true);

        table.add(fpsLabel).width(100);
        table.add(activeObjectLabel).width(100);

        stage.addActor(table);
    }

    public void update() {
        fpsLabel.setText("" + Gdx.graphics.getFramesPerSecond());
    }

    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}