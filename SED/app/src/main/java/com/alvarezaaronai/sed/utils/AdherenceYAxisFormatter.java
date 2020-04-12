package com.alvarezaaronai.sed.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class AdherenceYAxisFormatter extends ValueFormatter {

    private String[] hours = {"12am", "11pm", "10pm", "9pm", "8pm", "7pm",
        "6pm", "5pm", "4pm", "3pm", "2pm", "1pm", "12pm", "11am", "10am",
        "9am", "8am", "7am", "6am", "5am", "4am", "3am", "2am", "1am"};

    @Override
    public String getAxisLabel(float value, AxisBase axis) {

        int intValue = (int) Math.round(value);
        intValue -= 1;

        if(intValue < 0 || intValue > 23) {
            // Return empty string for axis label
            return "";
        }

        return hours[intValue];
    }
}
