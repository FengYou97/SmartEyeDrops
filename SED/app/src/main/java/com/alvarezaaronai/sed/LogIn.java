package com.alvarezaaronai.sed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogIn extends AppCompatActivity {
    Button logInButt;
    EditText userId, userPw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        logInButt = (Button)findViewById(R.id.signInButton);
        userId = (EditText)findViewById(R.id.userID);
        userPw = (EditText)findViewById(R.id.passwordID);

        logInButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userId.getText().toString().equals("doctor") &&
                        userPw.getText().toString().equals("doctorPW")) {
                    // change to doctor page instead of patient page activity
                    Intent doctorIntent = new Intent(LogIn.this,PatientPage.class);
                    startActivity(doctorIntent);
                    finish();
                }
                else if (userId.getText().toString().equals("patient") &&
                        userPw.getText().toString().equals("patientPW")){
                    Intent patientIntent = new Intent(LogIn.this,PatientPage.class);
                    startActivity(patientIntent);
                    finish();
                }else{
                    System.out.println("Not valid");
                }
            }
        });
    }
}
