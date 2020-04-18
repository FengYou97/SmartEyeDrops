package com.alvarezaaronai.sed.utils;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

public class AdherenceXAxisFormatter extends ValueFormatter {

    private static final String TAG = "AdherenceXAxisFormatter";

    /**
     * Was initially going to display all days of the month from
     * 1-31 (or 30 or 29 or whatever). But since the screen is so
     * small the numbers look all squished up together. So for now
     * I'm not putting any labels.
     */

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        /**
         * Subtracting 1 from intValue because I want it so that the first
         * has an empty string as a label, since it looks awkward if Mon
         * start at the very left corner
         */
//        int intValue = (int) Math.round(value);
//        intValue -= 1;
//
//        if(intValue < 0 || intValue > 6) {
//            // Return empty string for axis label
//            return "";
//        }

//        return days[intValue];
        return "";
    }


}
