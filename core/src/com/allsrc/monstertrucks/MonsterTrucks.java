package com.allsrc.monstertrucks;

import com.badlogic.gdx.ApplicationListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;

import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.utils.Disposable;

import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.Controller;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Pixmap;


public class MonsterTrucks implements ApplicationListener {

    public MonsterCamera camera;

	private boolean initialized;

    private MonsterListener monsterListener;

    public ModelBatch modelBatch;
    public SpriteBatch spriteBatch;

    private FrameBuffer buffer;
    private FrameBuffer dest;
    private FrameBuffer src;

    private ShaderProgram celShader;
    private Mesh fullScreenQuad;

	public void init() {
		if (initialized)
			return;

		Bullet.init();
		initialized = true;
	}

	@Override
	public void create () {
        /*
        celShader = new ShaderProgram(
            Gdx.files.internal("data/shaders/cel.vertex.glsl"),
            Gdx.files.internal("data/shaders/cel.fragment.glsl"));
        fullScreenQuad = createFullScreenQuad();

        buffer = new FrameBuffer(Pixmap.Format.RGBA8888,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        src = buffer;
        */
		init();

        Planet.EX.main = this;
        Planet.EX.settings = new Settings();
        Planet.EX.loader = new Loader();
        Planet.EX.world = new BulletWorld();

        loadAssets();

        modelBatch = new ModelBatch();
        spriteBatch = new SpriteBatch();        

		camera = new MonsterCamera(Planet.EX.settings.playerCount);

		for (int i = 0; i < Planet.EX.settings.playerCount; i++)
		{
			Planet.EX.cars.add((Car)new ParscheCar(new Vector3(5f, 3f, 0f), "blue"));

			if (i < Controllers.getControllers().size)
				Controllers.getControllers().get(i).addListener(Planet.EX.cars.get(i));
		}

        Planet.EX.editor = new Editor();

        Planet.EX.level = new Level();
        Planet.EX.level.init();
        Planet.EX.level.loadFromFile();
        Planet.EX.level.mode = new Race(4);    

        monsterListener = new MonsterListener();

		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
		inputMultiplexer.addProcessor(monsterListener);
		inputMultiplexer.addProcessor(Planet.EX.gui.stage);
        inputMultiplexer.addProcessor(Planet.EX.editor.editorListener);
	}

    public void loadAssets() {
        ParscheCar.load();
    }

	@Override
	public void render () {
        
		update();

		// dest = buffer;
		// dest.begin(); {
			
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            renderBackground();
        
			switch (Planet.EX.settings.playerCount) {
				case 1:
					renderScreen();
					break;
				case 2:
					renderSplitScreen();
					break;
				case 3:
				case 4:
					break;
			}
        /*
		}
		dest.end();

		src = dest;
		dest = buffer;

		src.getColorBufferTexture().bind(); {
    		celShader.begin();
        	fullScreenQuad.render(celShader, GL20.GL_TRIANGLE_FAN, 0, 4);
    		celShader.end();
    	}
        */

        Planet.EX.gui.render();
	}

    boolean t = false;
    int ii = 64;

    public void renderBackground() {
        if (t)
            ii++;
        t = !t;
        if (ii > 64)
            ii = 0;

        spriteBatch.begin();
        spriteBatch.draw(Planet.EX.level.background, 0f, 0f, 0f, 0f,
            (Gdx.graphics.getWidth() / 64) + 1,
            (Gdx.graphics.getHeight() / 64) + 1,
            64, 64,
            0f, // r
            ii, -ii,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
            false, false);
        spriteBatch.end();
    }

	public Mesh createFullScreenQuad(){
    	float[] verts = new float[16];
    	int i = 0;

    	verts[i++] = -1.f; // x1
    	verts[i++] = -1.f; // y1
    	verts[i++] =  0.f; // u1
    	verts[i++] =  0.f; // v1

    	verts[i++] =  1.f; // x2
    	verts[i++] = -1.f; // y2
    	verts[i++] =  1.f; // u2
    	verts[i++] =  0.f; // v2

    	verts[i++] =  1.f; // x3
    	verts[i++] =  1.f; // y2
    	verts[i++] =  1.f; // u3
    	verts[i++] =  1.f; // v3

    	verts[i++] = -1.f; // x4
    	verts[i++] =  1.f; // y4
    	verts[i++] =  0.f; // u4
    	verts[i++] =  1.f; // v4

    	Mesh mesh = new Mesh(true, 4, 0,
        	new VertexAttribute(Usage.Position, 2, "a_position"),
        	new VertexAttribute(Usage.TextureCoordinates,
        	2, "a_texCoord0"));
    	mesh.setVertices(verts);

    	return mesh;
	}

	public void renderScreen() {
        camera.focus(Planet.EX.cars.get(0));
		
		modelBatch.begin(camera.get());
		renderObjects();
		modelBatch.end();
	}

	public void renderSplitScreen() {
		camera.focus(Planet.EX.cars.get(0));
		modelBatch.begin(camera.get());
		Gdx.gl.glViewport(0, Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2);
		renderObjects();
		modelBatch.end();
		
		//

		camera.focus(Planet.EX.cars.get(1));
		modelBatch.begin(camera.get());
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2);
		renderObjects();
		modelBatch.end();
	}

	public void renderObjects() {
        Planet.EX.level.render();

		for (Car car : Planet.EX.cars) {
            car.update();

            if (camera.isVisible(car)) {
                modelBatch.render(car.entity.modelInstance, Planet.EX.level.environment);

                for (BulletEntity wheel : car.wheels)
                    modelBatch.render(wheel.modelInstance, Planet.EX.level.environment);
            }
        }
	}

	public void update () {
        Planet.EX.update();
	}

	@Override
	public void dispose () {
		Planet.EX.dispose();

		modelBatch.dispose();
		modelBatch = null;
	}

    @Override
    public void resize (int width, int height) {
    }

    @Override
    public void pause () {
    }

    @Override
    public void resume () {
    }
}
