package com.debruyckere.florian.go4lunch.Controller.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.debruyckere.florian.go4lunch.R;

public class SettingActivity extends AppCompatActivity {

    private Switch mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        configureSwitch();
    }

    /**
     * configure the switch to enable/disable notification
     */
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
