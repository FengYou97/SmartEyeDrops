package com.alvarezaaronai.sed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Decision extends AppCompatActivity {
    Button record, goToProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView(R.layout.activity_decision);

        record = (Button)findViewById(R.id.recordButton);
        goToProfile = (Button)findViewById(R.id.goToProfileButton);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doctorIntent = new Intent(Decision.this,MainActivity.class);
                startActivity(doctorIntent);
                finish();
            }
        });

        goToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change to the activity that has the profile i.e. physicians page
                Intent doctorIntent = new Intent(Decision.this,PatientPage.class);
                startActivity(doctorIntent);
                finish();
            }
        });
    }
}