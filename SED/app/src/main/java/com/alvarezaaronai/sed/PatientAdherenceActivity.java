package com.alvarezaaronai.sed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.alvarezaaronai.sed.utils.AdherenceXAxisFormatter;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

import java.util.ArrayList;
import java.util.List;

public class PatientAdherenceActivity extends AppCompatActivity {

    private static final String TAG = "PatientAdherenceActivit";

    private List<Entry> mEntries;
    private ScatterChart mScatterChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_adherence);

        mScatterChart = findViewById(R.id.adherence_scatter_chart);

        mEntries = generateEnrtries();
        // Create ScatterDataSet using list of Entries
        ScatterDataSet scatterDataSet1 = new ScatterDataSet(mEntries, "Test Entries");
        // Using the previous ScatterDataSet objects, add them to the list of IScatterDataSet objects
        List<IScatterDataSet> dataSetList = new ArrayList<IScatterDataSet>();
        dataSetList.add(scatterDataSet1);

        // Finally, we can create our ScatterData
        ScatterData scatterData = new ScatterData(dataSetList);

        mScatterChart.setData(scatterData);

        configureScatterChart();

        // IMPORTANT!! Don't forget!
        mScatterChart.invalidate(); // Refresh

    }

    private void configureScatterChart() {
        // Configure ScatterChart
        mScatterChart.setTouchEnabled(false); // Disables interaction with graph
        mScatterChart.getXAxis().setAxisMaximum(0.8f);
        mScatterChart.getXAxis().setAxisMinimum(0.0f);
        // Use the formatter
        mScatterChart.getXAxis().setValueFormatter(new AdherenceXAxisFormatter());
    }


    /**
     *  Gonna be using this to hard code some Entry values
     *  and returning a list of Entry objects.
     *
     *  XAxis:
     *      Mon = 0.1
     *      Tue = 0.2
     *      Wed = 0.3
     *      Thu = 0.4
     *      Fri = 0.5
     *      Sat = 0.6
     *      Sun = 0.7
     */
    private List<Entry> generateEnrtries() {
        // Entry(float x, float y)
        List<Entry> entries = new ArrayList<Entry>();

        Entry firstEntry = new Entry(0.1f,1.8f);
        Entry secondEntry = new Entry(0.4f,2f);

        entries.add(firstEntry);
        entries.add(secondEntry);


        return entries;
    }


}
