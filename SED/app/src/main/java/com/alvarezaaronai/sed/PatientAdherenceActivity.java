package com.alvarezaaronai.sed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.alvarezaaronai.sed.utils.AdherenceXAxisFormatter;
import com.alvarezaaronai.sed.utils.AdherenceYAxisFormatter;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PatientAdherenceActivity extends AppCompatActivity {

    private static final String TAG = "PatientAdherenceActivit";

    private List<Entry> mScheduledEntries;
    private List<Entry> mPatientEntries;
    private ScatterChart mScatterChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_adherence);

        // Check if we got extra
        if(getIntent().hasExtra("patient_id")) {
            // If we get -1, something is obviously wrong.
            int patient_id = getIntent().getIntExtra("patient_id" , -1);
            Log.d(TAG, "onCreate: Patient Id Extra: " + patient_id);
        }

        mScatterChart = findViewById(R.id.adherence_scatter_chart);

        mScheduledEntries = generateScheduledEntries();

        // Random values for Patient
        mPatientEntries = generateRandomEntries();

        // Create ScatterDataSet using list of Entries
        ScatterDataSet scatterDataSet1 = new ScatterDataSet(mScheduledEntries, "Scheduled");
        scatterDataSet1.setColor(R.color.grey);

        // Create ScatterDataSet for random patient entries
        ScatterDataSet randomDataSet = new ScatterDataSet(mPatientEntries, "Actual");

        // Using the previous ScatterDataSet objects, add them to the list of IScatterDataSet objects
        List<IScatterDataSet> dataSetList = new ArrayList<IScatterDataSet>();
        dataSetList.add(scatterDataSet1);
        dataSetList.add(randomDataSet);

        // Finally, we can create our ScatterData
        ScatterData scatterData = new ScatterData(dataSetList);
        // Removes Text from data values (Ex: 18 for 7am)
        scatterData.setDrawValues(false);

        mScatterChart.setData(scatterData);

        configureScatterChart();

        // IMPORTANT!! Don't forget!
        mScatterChart.invalidate(); // Refresh

    }

    private void configureScatterChart() {
        // Configure ScatterChart
        mScatterChart.setTouchEnabled(false); // Disables interaction with graph
        mScatterChart.getDescription().setEnabled(false);

        // YAxis
        mScatterChart.getAxisRight().setEnabled(false);// Remove right labels
        YAxis yLeftAxis = mScatterChart.getAxisLeft();
        yLeftAxis.setAxisMaximum(25.0f);
        yLeftAxis.setAxisMinimum(0.0f);
        // Will allow us to see 24 labels. Which we'll use to display all 24 hours of a day
        yLeftAxis.setLabelCount(25);
        yLeftAxis.setValueFormatter(new AdherenceYAxisFormatter());

        // XAxis
        mScatterChart.getXAxis().setAxisMaximum(8.0f);
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
    private List<Entry> generateScheduledEntries() {
        // Entry(float x, float y)
        List<Entry> entries = new ArrayList<Entry>();

        for(int i = 1; i < 8; i++) {
            entries.add(new Entry((float) i, 18f));
        }

        return entries;
    }

    // For prototyping purposes only. Delete later
    private List<Entry> generateRandomEntries() {
        List<Entry> entries = new ArrayList<Entry>();

        for(int i = 1; i < 8; i++) {
            float hourOfDay = (float) getRandomNumberInRange(1, 24);
            entries.add(new Entry((float) i, hourOfDay));
        }

        return entries;
    }

    // Helper Function for generateRandomEntries(). Delete later
    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


}
