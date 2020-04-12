package com.alvarezaaronai.sed.utils;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

public class AdherenceXAxisFormatter extends ValueFormatter {

    private static final String TAG = "AdherenceXAxisFormatter";

    /**
     * Okay so this works so far.
     * We just need to disable zoom or only allow 7 value on the graph.
     * Because now we're getting
     * "Mon" "Mon"  "Tue" ...etc
     */
    private String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        /**
         * Subtracting 1 from intValue because I want it so that the first
         * has an empty string as a label, since it looks awkward if Mon
         * start at the very left corner
         */
        int intValue = (int) Math.round(value);
        intValue -= 1;

        if(intValue < 0 || intValue > 6) {
            // Return empty string for axis label
            return "";
        }

        return days[intValue];
    }


}
