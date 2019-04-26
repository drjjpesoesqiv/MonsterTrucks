package com.allsrc.monstertrucks;

import com.badlogic.gdx.graphics.Color;

public class MonsterColor {
    public static final Color CYAN = new Color(0f, 1f, 1f, 1f);
    public static final Color MAGENTA = new Color(1f, 1f, 0f, 1f);
    public static final Color YELLOW = new Color(1f, 0f, 1f, 1f);

    public static Color randomColor() {
        return new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1f);
    }
}