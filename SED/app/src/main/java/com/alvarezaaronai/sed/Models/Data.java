package com.alvarezaaronai.sed.Models;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Data {
    private String timeStamp = "";
    private Long xAccel = new Long(0);
    private Long yAccel = new Long(0);
    private Long zAccel = new Long(0);

    public Data(String timeStamp, Long xAccel, Long yAccel, Long zAccel) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
        //format timestamp
        System.out.println(sdf.format(timestamp));
        this.timeStamp =  "" + sdf.format(timestamp);
        this.xAccel = xAccel;
        this.yAccel = yAccel;
        this.zAccel = zAccel;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public Long getxAccel() {
        return xAccel;
    }

    public Long getyAccel() {
        return yAccel;
    }

    public Long getzAccel() {
        return zAccel;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setxAccel(Long xAccel) {
        this.xAccel = xAccel;
    }

    public void setyAccel(Long yAccel) {
        this.yAccel = yAccel;
    }

    public void setzAccel(Long zAccel) {
        this.zAccel = zAccel;
    }

    @Override
    public String toString() {
        return "Data{" +
                "timeStamp='" + timeStamp + '\'' +
                ", xAccel=" + xAccel +
                ", yAccel=" + yAccel +
                ", zAccel=" + zAccel +
                '}';
    }
}
