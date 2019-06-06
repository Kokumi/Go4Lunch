package com.debruyckere.florian.go4lunch.Controller.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;

import com.debruyckere.florian.go4lunch.R;

/**
 * Created by Debruyckère Florian on 07/01/2019.
 */
public class BaseFragment extends Fragment {

    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() != null)
        mContext = getActivity().getApplicationContext();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getActivity() != null)
            getActivity().getMenuInflater().inflate(R.menu.actionbar_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
