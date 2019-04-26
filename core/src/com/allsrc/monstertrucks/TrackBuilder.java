package com.allsrc.monstertrucks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class TrackBuilder {
    public Array<Track> parts = new Array<Track>();

    private int northWest = 270;
    private int northEast = 180;
    private int southEast = 90;
    private int southWest = 0;

    private Vector3 next = new Vector3(0, 0, 0);
    private Vector3 dir = new Vector3(0, 0, 1);
    private int size = 8;

    public Vector2 straight() {
        Track temp = new Track();
        temp.setPos(next.cpy());

        if (dir.x != 0) {
            next.x += size * dir.x;
            if (dir.x == 1)
                temp.setRot(0);
            else
                temp.setRot(180);
        }
        else {
            next.z += size * dir.z;
            if (dir.z == 1)
                temp.setRot(-90);
            else
                temp.setRot(90);
        }

        temp.construct("straight");

        temp.updateRot();
        temp.updatePos();

        parts.add(temp);

        return new Vector2(temp.getPos().x, temp.getPos().z);
    }

    // 0 left, 1 right
    public Vector2 turn(int d) {
        Track temp = new Track();
        temp.setPos(next.cpy());

        switch (d) {
            case 0:
                if (dir.x != 0) {
                    if (dir.x == 1) {
                        temp.setRot(northWest);
                        dir.z = -1;
                    }
                    else {
                        temp.setRot(southEast);
                        dir.z = 1;
                    }
                    next.z += dir.z * size;
                    dir.x = 0;
                }
                else {
                    if (dir.z == 1) {
                        temp.setRot(northEast);
                        dir.x = 1;
                    }
                    else {
                        temp.setRot(southWest);
                        dir.x = -1;
                    }
                    next.x += dir.x * size;
                    dir.z = 0;
                }
                break;
            case 1:
                if (dir.x != 0) {
                    if (dir.x == 1) {
                        temp.setRot(southWest);
                        dir.z = 1;
                    }
                    else {
                        temp.setRot(northEast);
                        dir.z = -1;
                    }
                    next.z += dir.z * size;
                    dir.x = 0;
                }
                else {
                    if (dir.z == 1) {
                        temp.setRot(northWest);
                        dir.x = -1;
                    }
                    else {
                        temp.setRot(southEast);
                        dir.x = 1;
                    }
                    next.x += dir.x * size;
                    dir.z = 0;
                }
                break;
        }

        temp.construct("turn");

        temp.updateRot();
        temp.updatePos();

        parts.add(temp);

        return new Vector2(temp.getPos().x, temp.getPos().z);
    }

    public void clean() {
        next = null;
        dir = null;
    }
}
