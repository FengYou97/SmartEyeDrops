package com.alvarezaaronai.sed.Models;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class SensorSession {
    List<Data> dataList;
    String timeStamp;
    StringBuilder dataString;

    public SensorSession() {

        this.dataList = new ArrayList<Data>() {};
        //Creating a Time Stamp
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
        //format timestamp
        timeStamp = sdf.format(timestamp);
        System.out.println(timeStamp);

        dataString = new StringBuilder("time,x,y,z,force\n");

    }

    public boolean addData(Data dataInput){
        if(dataList == null){
            System.out.println("List Is Empty");
            return false;
        }
        if(dataList.add(dataInput)){
            dataString.append(dataInput.toString());
            //This will add all data onto a String.
        }
        //Else Add Data
        return false;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public StringBuilder getDataString() {
        return dataString;
    }

    public void setDataString(StringBuilder dataString) {
        this.dataString = dataString;
    }

    @Override
    public String toString() {
        return "SensorSession{" +
                "Sensor Accel Data =" + dataList +
                ", timeStamp='" + timeStamp + '\n' +
                '}';
    }
}
