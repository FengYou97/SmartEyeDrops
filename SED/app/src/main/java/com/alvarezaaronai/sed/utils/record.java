package com.alvarezaaronai.sed.utils;

import java.util.Date;

public class record {
    public record(String _date, String _time){
        date = _date;
        time = _time;
    }

    private String date;
    private String time;

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString(){
        return ("\n\tDate: "  + date + "\n\tTime: " + time);
    }

}
