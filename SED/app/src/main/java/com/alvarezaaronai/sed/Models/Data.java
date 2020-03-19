package com.alvarezaaronai.sed.Models;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Data {
    private String timeStamp;
    private Float xAccel ;
    private Float yAccel ;
    private Float zAccel ;
    private Float force ;

    public Data(String timeStamp, Float xAccel, Float yAccel, Float zAccel, Float force) {
        this.timeStamp = timeStamp;
        this.xAccel = xAccel;
        this.yAccel = yAccel;
        this.zAccel = zAccel;
        this.force = force;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public Float getxAccel() {
        return xAccel;
    }

    public Float getyAccel() {
        return yAccel;
    }

    public Float getzAccel() {
        return zAccel;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setxAccel(Float xAccel) {
        this.xAccel = xAccel;
    }

    public void setyAccel(Float yAccel) {
        this.yAccel = yAccel;
    }

    public void setzAccel(Float zAccel) {
        this.zAccel = zAccel;
    }

    public Float getForce() {
        return force;
    }

    public void setForce(Float force) {
        this.force = force;
    }

    @Override
    public String toString() {
        return  ""  + timeStamp +
                "," + xAccel +
                "," + yAccel +
                "," + zAccel +
                "," + force +
                '\n';
    }
}
