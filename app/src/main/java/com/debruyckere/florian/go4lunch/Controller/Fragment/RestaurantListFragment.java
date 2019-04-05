package com.debruyckere.florian.go4lunch.Controller.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debruyckere.florian.go4lunch.Model.RestaurantAdapter;
import com.debruyckere.florian.go4lunch.R;


public class RestaurantListFragment extends BaseFragment {

    public RestaurantListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RestaurantListFragment.
     */
    public static RestaurantListFragment newInstance() {
        RestaurantListFragment fragment = new RestaurantListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        RecyclerView recycler = view.findViewById(R.id.list_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recycler.setAdapter(new RestaurantAdapter());

        return view;
    }
}
