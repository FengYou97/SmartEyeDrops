package com.alvarezaaronai.sed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Use this to send to Server: replace "t1" with the data in String format
        //place this part of the code in the area where data is collected so that it can be sent
        String t1 = "time,x,y,z,force\n" +
                "10,0.2,0.2,0.2,100";
        new Client().execute(t1);
    }


}
