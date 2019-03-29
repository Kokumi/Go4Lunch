package com.debruyckere.florian.go4lunch.Controller.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.debruyckere.florian.go4lunch.Model.ViewPagerAdapter;
import com.debruyckere.florian.go4lunch.R;

public class appActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        configureViewPager();
    }

    private void configureViewPager(){
        ViewPager pager = findViewById(R.id.app_viewpager);

        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        TabLayout tabs=findViewById(R.id.app_tablayout);

        tabs.setupWithViewPager(pager);

        tabs.setTabMode(TabLayout.MODE_FIXED);
    }
}
