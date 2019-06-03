package com.debruyckere.florian.go4lunch.Controller.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.debruyckere.florian.go4lunch.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

/**
 * Created by Debruyckère Florian on 07/01/2019.
 */
public class BaseFragment extends Fragment {

    protected MaterialSearchView mMaterialSearchView;
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
        MenuItem item = menu.findItem(R.id.action_search);
        mMaterialSearchView.setMenuItem(item);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
