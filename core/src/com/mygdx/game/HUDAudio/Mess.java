package com.mygdx.game.HUDAudio;

public class Mess {
    private String mess;
    private float timeLife;

    public Mess() {
        this.mess = "-";
        timeLife = 0;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public float getTimeLife() {
        return timeLife;
    }

    public void setTimeLife(float timeLife) {
        this.timeLife = timeLife;
    }

    @Override
    public String toString() {
        return "Mess{" +
                "mess='" + mess + '\'' +
                ", timeLife=" + timeLife +
                '}';
    }
}
