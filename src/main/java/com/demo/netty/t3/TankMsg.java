package com.demo.netty.t3;

public class TankMsg {

    public int x;

    public int y;

    public TankMsg(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "TankMsg:" + x + "," + y;
    }
}
