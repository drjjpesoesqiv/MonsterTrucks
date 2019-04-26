package com.allsrc.monstertrucks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

class Path {
    String id;
    Array<Vector2> nodes = new Array<Vector2>();

    public Path(String id) {
        this.id = id;
    }

    public Path(String id, Vector2[] nodes) {
        this.id = id;
        for (int i = 0; i < nodes.length; i++)
            this.nodes.add(nodes[i]);
    }

    public void addNode(Vector2 node) {
        nodes.add(node);
    }

    public Vector2 getNode(int i) {
        return nodes.get(i);
    }
}