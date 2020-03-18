package com.alvarezaaronai.sed.Models;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SensorSession {
    List<Data> dataList;
    String timeStamp;

    public SensorSession() {

        this.dataList = new ArrayList<Data>() {};
        //Creating a Time Stamp
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
        //format timestamp
        timeStamp = sdf.format(timestamp);
        System.out.println(timeStamp);

    }

    public boolean addData(Data dataInput){
        if(dataList == null){
            System.out.println("List Is Empty");
            return false;
        }
        //Else Add Data
        return dataList.add(dataInput);
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
