package com.allsrc.monstertrucks;

public class Stopwatch {
    static String name = "";
    static long millis = 0;

    static public void start(String _n) {
        name = _n;
        millis = System.currentTimeMillis() % 1000;
    }

    static public void stop() {
        System.out.println(name + ' ' + ((System.currentTimeMillis() % 1000) - millis));
    }
}