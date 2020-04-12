package com.alvarezaaronai.sed;

import androidx.appcompat.app.AppCompatActivity;
import gr.net.maroulis.library.EasySplashScreen;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.alvarezaaronai.sed.Models.Physician;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        EasySplashScreen config = new EasySplashScreen(SplashActivity.this)
//                .withFullScreen()
//                .withTargetActivity(MainActivity.class)
//                .withSplashTimeOut(3000)
//                .withAfterLogoText("Smart Eye Drops")
//                .withFooterText("Sponsored by Vodafone")
//                .withLogo(R.mipmap.ic_launcher_eye_drop_foreground);
        // Changing this just to test out the ScatterChart
        EasySplashScreen config = new EasySplashScreen(SplashActivity.this)
                .withFullScreen()
                .withTargetActivity(PhysicianProfileActivity.class)
                .withSplashTimeOut(3000)
                .withAfterLogoText("Smart Eye Drops")
                .withFooterText("Sponsored by Vodafone")
                .withLogo(R.mipmap.ic_launcher_eye_drop_foreground);

        config.getFooterTextView().setTextColor(Color.GRAY);
        config.getAfterLogoTextView().setTextSize(30);
        config.getAfterLogoTextView().setTextColor(Color.BLACK);


        View easySplahScreen = config.create();
        setContentView(easySplahScreen);
    }
}
