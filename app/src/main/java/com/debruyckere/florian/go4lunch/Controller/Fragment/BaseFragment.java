package com.debruyckere.florian.go4lunch.Controller.Fragment;

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

    MaterialSearchView mMaterialSearchView;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getActivity() != null)
            getActivity().getMenuInflater().inflate(R.menu.actionbar_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mMaterialSearchView.setMenuItem(item);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
