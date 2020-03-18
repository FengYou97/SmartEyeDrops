package com.alvarezaaronai.sed.Models;

import java.util.List;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SensorSession {
    List<Data> dataList;
    String timeStamp;

    public SensorSession(List<Data> dataList) {
        this.dataList = dataList;
        //Creating a Time Stamp
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
        //format timestamp
        System.out.println(sdf.format(timestamp));
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "SensorSession{" +
                "dataList=" + dataList +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
