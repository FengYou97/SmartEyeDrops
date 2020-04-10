package com.alvarezaaronai.sed;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PatientPage extends AppCompatActivity {
    private Button signOut, chartResults, realTime;
    private View graphs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_page);

        signOut = (Button)findViewById(R.id.patientSignOutButton);
        chartResults= (Button)findViewById(R.id.chartResults);
        realTime= (Button)findViewById(R.id.realTime);

        graphs = findViewById(R.id.graphResults);


    }

    //TODO display graph in View when clicking on appropriate button
}
