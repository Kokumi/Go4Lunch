package com.debruyckere.florian.go4lunch.Controller.Activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.debruyckere.florian.go4lunch.Model.Notification;
import com.debruyckere.florian.go4lunch.R;
import com.google.android.gms.common.api.Api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SettingActivity extends AppCompatActivity {

    private Switch mSwitch;
    private static String CHANNEL_ID = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        configureSwitch();
    }

    private void configureSwitch(){
        mSwitch = findViewById(R.id.setting_switch);

        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: configure notification

                getSharedPreferences("notificationSet",MODE_PRIVATE).edit()
                        .putBoolean("notification",mSwitch.isChecked())
                        .apply();
            }
        });

        try {
            SharedPreferences preferences = getSharedPreferences("notificationSet", MODE_PRIVATE);
            mSwitch.setChecked(preferences.getBoolean("notification",false));
        }catch (Exception e){
            Log.i("NotificationPreferences","no preferences found");
        }
    }


}
