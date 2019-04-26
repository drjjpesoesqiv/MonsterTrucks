package com.allsrc.monstertrucks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDefaultVehicleRaycaster;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRaycastVehicle;
import com.badlogic.gdx.physics.bullet.dynamics.btRaycastVehicle.btVehicleTuning;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btVehicleRaycaster;
import com.badlogic.gdx.physics.bullet.dynamics.btWheelInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btTransform;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.mappings.Ouya;

public class Car extends BulletObject implements ControllerListener {
    public Matrix4 worldTransform = new Matrix4();
    public Vector3 worldTranslation = new Vector3();

    private Vector3 tmpV = new Vector3();

    protected ObjLoader objLoader = new ObjLoader();

    protected btVehicleRaycaster raycaster;
    protected btRaycastVehicle vehicle;
    protected btVehicleTuning tuning;

    protected BulletEntity chassis;
    protected BulletEntity wheels[] = new BulletEntity[4];

    protected float currentForce = 0f;
    protected float currentAngle = 0f;

    protected float maxForce = 50f;
    protected float acceleration = 150f; // second
    protected float maxAngle = 25f;
    protected float steerSpeed = 65f; // second

    protected float frictionSlip = 125f;
    protected float maxSuspensionForce = 8000f;
    protected float maxSuspensionTravelCm = 50f;
    protected float suspensionCompression = 2.4f;
    protected float suspensionDamping = 2.3f;
    protected float suspensionStiffness = 20f;

    protected Model chassisModel;
    protected Model wheelModel;

    protected Vector3 chassisScale = new Vector3(1f, 1f, 1f);
    protected Vector3 wheelScale = new Vector3(1f, 1f, 1f);

    protected Vector3 chassisHalfExtents;
    protected Vector3 wheelHalfExtents;

    protected Vector3 initPos;

    // left axii of any controller
    public float horzAxis = 0;
    public float vertAxis = 0;

    protected boolean upPressed;
    protected boolean downPressed;
    protected boolean leftPressed;
    protected boolean rightPressed;

    protected boolean paused = false;

    btTransform[] wheelTransform = new btTransform[4];
    btWheelInfo[] wheelInfo = new btWheelInfo[4];

    String color;
    String chassisName;
    String wheelName;

    Vector2 target = new Vector2();

    public void setTarget(Vector2 target) {
        this.target = target;
    }

    public Car(Vector3 pos, String color) {
        setPos(pos);
        initPos = pos;

        this.color = color;
    }

    protected void loadAssets() {
        Model cModel = Planet.EX.loader.getModel(chassisName);
        Model wModel = Planet.EX.loader.getModel(wheelName);

        BoundingBox bounds = new BoundingBox();

        chassisHalfExtents = new Vector3(cModel.calculateBoundingBox(bounds).getDimensions()).scl(0.5f);
        wheelHalfExtents = new Vector3(wModel.calculateBoundingBox(bounds).getDimensions()).scl(0.5f);

        Planet.EX.world.addConstructor(chassisName, new BulletConstructor(cModel, 100f, new btBoxShape(chassisHalfExtents)));
        Planet.EX.world.addConstructor(wheelName, new BulletConstructor(wModel, 0, null));

        entity = Planet.EX.world.add(chassisName, initPos.x, initPos.y, initPos.z);
        wheels[0] = Planet.EX.world.add(wheelName, 0, 0f, 0);
        wheels[1] = Planet.EX.world.add(wheelName, 0, 0f, 0);
        wheels[2] = Planet.EX.world.add(wheelName, 0, 0f, 0);
        wheels[3] = Planet.EX.world.add(wheelName, 0, 0f, 0);
        
        entity.modelInstance.materials.get(0).set(Planet.EX.loader.getTextureAttribute(chassisName + "_" + color));
    }

    protected void init() {
        loadAssets();
        
        setTuning();
        raycaster = new btDefaultVehicleRaycaster((btDynamicsWorld)Planet.EX.world.collisionWorld);
        vehicle = new btRaycastVehicle(tuning, (btRigidBody)entity.body, raycaster);

        ((btDynamicsWorld)Planet.EX.world.collisionWorld).addVehicle(vehicle);

        vehicle.setCoordinateSystem(0, 1, 2);

        addWheels();

        entity.body.setActivationState(Collision.DISABLE_DEACTIVATION);
    }

    public void pause() {
        paused = !paused;
    }

    public void setTuning() {
        tuning = new btVehicleTuning();
        tuning.setFrictionSlip(frictionSlip);
        tuning.setMaxSuspensionForce(maxSuspensionForce);
        tuning.setMaxSuspensionTravelCm(maxSuspensionTravelCm);
        tuning.setSuspensionCompression(suspensionCompression);
        tuning.setSuspensionDamping(suspensionDamping);
        tuning.setSuspensionStiffness(suspensionStiffness);
    }

    public void addWheels() {
        Vector3 point = new Vector3();
        Vector3 direction = new Vector3(0, -1, 0);
        Vector3 axis = new Vector3(-1, 0, 0);

        vehicle.addWheel(point.set(chassisHalfExtents).scl(0.95f, -1f, 0.7f), direction, axis, wheelHalfExtents.z * 0.2f, wheelHalfExtents.z, tuning, true);
        vehicle.addWheel(point.set(chassisHalfExtents).scl(-0.95f, -1f, 0.7f), direction, axis, wheelHalfExtents.z * 0.2f, wheelHalfExtents.z, tuning, true);
        vehicle.addWheel(point.set(chassisHalfExtents).scl(0.95f, -1f, -0.475f), direction, axis, wheelHalfExtents.z * 0.2f, wheelHalfExtents.z, tuning, false);
        vehicle.addWheel(point.set(chassisHalfExtents).scl(-0.95f, -1f, -0.475f), direction, axis, wheelHalfExtents.z * 0.2f, wheelHalfExtents.z, tuning, false);

        for (int i = 0; i < wheels.length; i++) {
            wheelInfo[i] = vehicle.getWheelInfo(i);
            wheelTransform[i] = wheelInfo[i].getWorldTransform();

            wheelInfo[i].setRollInfluence(0f);
            wheelInfo[i].setWheelsDampingCompression(8f);
            wheelInfo[i].setWheelsDampingRelaxation(12f);
        }
    }

    float angle = 0;
    float force = 0;
    float delta = 0;
    float impulseScale = 1f;
    Matrix4 m1 = new Matrix4();
    Matrix4 m2 = new Matrix4();
    boolean isOnGround = false;

    public void update() {
        entity.motionState.getWorldTransform(worldTransform);
        worldTransform.getTranslation(worldTranslation);
        
        applyEngineForce();
        setSteeringValue();

        isOnGround = checkIfOnGround();

        /*
        if (!isOnGround) {
            m2.set(m1);

            if (horzAxis != 0) {
                m2.rotate(((btRigidBody)(entity.body)).getOrientation());
                m2.translate(new Vector3(0, 0, horzAxis * (impulseScale / 2)));

                ((btRigidBody)(entity.body)).applyTorqueImpulse(m2.getTranslation(tmpV));
            }

            if (vertAxis != 0) {
                m2.rotate(((btRigidBody)(entity.body)).getOrientation());
                m2.translate(new Vector3(-1 * vertAxis * impulseScale * 2, 0, 0));

                ((btRigidBody)(entity.body)).applyTorqueImpulse(m2.getTranslation(tmpV));
            }
        }
        */
    }

    public void updateForce() {
        delta = Gdx.graphics.getDeltaTime();

        if (upPressed) {
            if (force < 0) force = 0;
            force = MathUtils.clamp(force + acceleration * delta, 0, maxForce);
        } else if (downPressed) {
            if (force > 0) force = 0;
            force = MathUtils.clamp(force - acceleration * delta, -maxForce, 0);
        } else
            force = 0;
    }

    public void applyEngineForce() {
        updateForce();

        vehicle.applyEngineForce(force, 0);
        vehicle.applyEngineForce(force, 1);
        // vehicle.applyEngineForce(force, 2);
        // vehicle.applyEngineForce(force, 3);
    }

    public void setSteeringValue() {
        currentAngle = MonsterUtils.map(horzAxis, 1f, -1f, -maxAngle, maxAngle);
        vehicle.setSteeringValue(currentAngle * MathUtils.degreesToRadians, 0);
        vehicle.setSteeringValue(currentAngle * MathUtils.degreesToRadians, 1);
    }

    public boolean checkIfOnGround() {
        boolean onGround = false;

        for (int i = 0; i < wheels.length; i++) {
            vehicle.updateWheelTransform(i, true);
            wheelTransform[i].getOpenGLMatrix(wheels[i].transform.val);
            // allocation problem
            // if (wheelInfo[i].getRaycastInfo().getGroundObject() != 0)
            //     isOnGround = true;
        }

        return onGround;
    }

    public void reset() {
        entity.body.setWorldTransform(entity.transform.setToTranslation(initPos));
        entity.body.setInterpolationWorldTransform(entity.transform);
        ((btRigidBody)(entity.body)).setLinearVelocity(Vector3.Zero);
        ((btRigidBody)(entity.body)).setAngularVelocity(Vector3.Zero);
        entity.body.activate();

        horzAxis = 0;
        vertAxis = 0;
    }

    public void render() {
        Planet.EX.main.modelBatch.render(entity.modelInstance, Planet.EX.level.environment);
        for (BulletEntity wheel : wheels)
            Planet.EX.main.modelBatch.render(wheel.modelInstance, Planet.EX.level.environment);
    }

    // Controllers
    @Override
    public void connected(Controller controller) {
        // TODO Auto-generated method stub
    }

    @Override
    public void disconnected(Controller controller) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        /*
        if (axisCode == 0) {
            if (value < 0.1f && value > -0.1f)
                currentAngle = 0;
            else
                currentAngle = MonsterUtils.map(value, 1f, -1f, -maxAngle, maxAngle);
        }

        if (axisCode == 0)
            horzAxis = (value < 0.1f && value > -0.1f) ? 0 : value;

        else if (axisCode == 1)
            vertAxis = (value < 0.1f && value > -0.1f) ? 0 : value;
        */
        return false;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (buttonCode == 1 || buttonCode == 14 || buttonCode == Ouya.BUTTON_O)
            upPressed = true;

        if (buttonCode == 0 || buttonCode == 15 || buttonCode == Ouya.BUTTON_U)
            downPressed = true;

        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        if (buttonCode == 3 || buttonCode == 12 || 
            buttonCode == Ouya.BUTTON_Y)
            reset();

        if (buttonCode == 1 || buttonCode == 14 || 
            buttonCode == Ouya.BUTTON_O)
            upPressed = false;

        if (buttonCode == 0 || buttonCode == 15 || 
            buttonCode == Ouya.BUTTON_U)
            downPressed = false;

        return false;
    }

    //

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        // TODO Auto-generated method stub
        return false;
    }
}