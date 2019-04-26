package com.allsrc.monstertrucks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;

public class Loader {

    AssetManager manager = new AssetManager();

    public class ModelInfo {
        Model model;
        btBvhTriangleMeshShape meshShape;
        Vector3 center = new Vector3();
        float radius;
    }

    public class TextureInfo {
        Texture texture;
        TextureAttribute textureAttribute;
    }

    private ObjLoader objLoader = new ObjLoader();

    private HashMap<String,Sound> sounds = new HashMap<String,Sound>();
    private HashMap<String,ModelInfo> models = new HashMap<String,ModelInfo>();
    private HashMap<String,TextureInfo> textures = new HashMap<String,TextureInfo>();

    public void clear() {
        sounds.clear();
        models.clear();
        textures.clear();
    }

    public Vector3 getCenter(String name) {
        return models.get(name).center;
    }

    public float getRadius(String name) {
        return models.get(name).radius;
    }

    // temp
    private Vector3 dimensions = new Vector3();
    private BoundingBox bounds = new BoundingBox();

    public void addModel(String name, Model model) {
        ModelInfo info = new ModelInfo();
        info.model = model;
        models.put(name, info);
        examineModel(name);
    }

    public void loadModel(String name, String file) {
        ModelInfo info = new ModelInfo();
        info.model = objLoader.loadModel(Gdx.files.internal(file), true);
        models.put(name, info);
        examineModel(name);
    }

    public void loadModel(String name, String file, boolean managed) {
        manager.load(file, Model.class);
        manager.finishLoading();

        ModelInfo info = new ModelInfo();
        info.model = manager.get(file);
        models.put(name, info);
        examineModel(name);
    }

    public void examineModel(String name) {
        ModelInfo info = models.get(name);

        info.model.calculateBoundingBox(bounds);
        info.center.set(bounds.getCenter());
        dimensions.set(bounds.getDimensions());
        info.radius = dimensions.len() / 2f;

        models.put(name, info);
    }

    public void addMeshShape(String name) {
        models.get(name).meshShape = new btBvhTriangleMeshShape(
            models.get(name).model.meshParts);
    }

    public void loadSound(String name, String file) {
        sounds.put(name, Gdx.audio.newSound(Gdx.files.internal(file)));
    }

    public void loadTexture(String name, String file) {
        TextureInfo info = new TextureInfo();

        info.texture = new Texture(Gdx.files.internal(file), true);
        info.textureAttribute = TextureAttribute.createDiffuse(info.texture);

        textures.put(name, info);
    }

    public void loadTexture(String name, String file, boolean managed) {
        manager.load(file, Texture.class);
        manager.finishLoading();

        TextureInfo info = new TextureInfo();

        info.texture = manager.get(file);
        info.textureAttribute = TextureAttribute.createDiffuse(info.texture);

        textures.put(name, info);
    }

    public Model getModel(String name) {
        return models.get(name).model;
    }

    public Sound getSound(String name) {
        return sounds.get(name);
    }

    public TextureAttribute getTextureAttribute(String name) {
        return textures.get(name).textureAttribute;
    }
}