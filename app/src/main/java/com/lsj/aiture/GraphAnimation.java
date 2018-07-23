package com.lsj.aiture;

/**
 * Created by kyyet on 2018-07-23.
 */

public class GraphAnimation {
    public static final int LINEAR_ANIMATION = 1;
    public static final int DEFAULT_DURATION = 2000;
    private int animation = 1;
    private int duration = 2000;

    public GraphAnimation() {
    }

    public GraphAnimation(int animation, int duration) {
        this.animation = animation;
        this.duration = duration;
    }

    public int getAnimation() {
        return this.animation;
    }

    public void setAnimation(int animation) {
        this.animation = animation;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}