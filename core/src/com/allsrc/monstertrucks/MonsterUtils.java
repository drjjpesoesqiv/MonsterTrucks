package com.allsrc.monstertrucks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;

public final class MonsterUtils {
    static public float map(float value, float low1, float high1, float low2, float high2) {
        return low2 + (high2 - low2) * (value - low1) / (high1 - low1);
    }

    static public float map(int value, float low1, float high1, float low2, float high2) {
        return low2 + (high2 - low2) * (value - low1) / (high1 - low1);
    }

    static public boolean isMobile() {
        return (Gdx.app.getType() == ApplicationType.Android ||
            Gdx.app.getType() == ApplicationType.iOS);
    }
}