package com.alvarezaaronai.sed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

import java.util.ArrayList;
import java.util.List;

public class PatientAdherenceActivity extends AppCompatActivity {


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
        // IMPORTANT!! Don't forget!
        mScatterChart.invalidate(); // Refresh

    }

    /**
     *  Gonna be using this to hard code some Entry values
     *  and returning a list of Entry objects.
     */
    private List<Entry> generateEnrtries() {
        // Entry(float x, float y)
        List<Entry> entries = new ArrayList<Entry>();

        Entry firstEntry = new Entry(1f,1f);
        Entry secondEntry = new Entry(2f,2f);
        Entry thirdEntry = new Entry(2f,2f);
        entries.add(firstEntry);
        entries.add(secondEntry);
        entries.add(thirdEntry);

        return entries;
    }


}
