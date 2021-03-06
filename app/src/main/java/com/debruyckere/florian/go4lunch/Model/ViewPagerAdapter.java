package com.debruyckere.florian.go4lunch.Model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.debruyckere.florian.go4lunch.Controller.Fragment.BaseFragment;
import com.debruyckere.florian.go4lunch.Controller.Fragment.ColleagueListFragment;
import com.debruyckere.florian.go4lunch.Controller.Fragment.RestaurantListFragment;
import com.debruyckere.florian.go4lunch.Controller.Fragment.RestaurantMapFragment;

/**
 * Created by Debruyckère Florian on 04/01/2019.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment bF;
        switch(position){
            case 0: bF = RestaurantMapFragment.newInstance();
                break;
            case 1: bF= RestaurantListFragment.newInstance();
                break;
            case 2: bF = ColleagueListFragment.newInstance();
                break;
            default: bF= RestaurantMapFragment.newInstance();
                break;
        }

        return bF;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position){
        CharSequence title;

        switch (position){
            default:title="Default Title";
            break;
            case 0:title="Map View";
            break;
            case 1:title="List View";
            break;
            case 2:title="Workmates";
                break;
        }

        return title;
    }
}
