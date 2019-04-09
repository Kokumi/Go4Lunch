package com.debruyckere.florian.go4lunch.Controller.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debruyckere.florian.go4lunch.Model.FireBaseConnector;
import com.debruyckere.florian.go4lunch.Model.Restaurant;
import com.debruyckere.florian.go4lunch.Model.RestaurantAdapter;
import com.debruyckere.florian.go4lunch.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;

import java.util.ArrayList;


public class RestaurantListFragment extends BaseFragment {

    private RecyclerView mRecycler;
    private Context mContext;

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
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        new FireBaseConnector().getRestaurantsData(getListener(),mContext);

        mRecycler = view.findViewById(R.id.list_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));


        return view;
    }

    public OnSuccessListener getListener(){

        return new OnSuccessListener<FindCurrentPlaceResponse>() {
            @Override
            public void onSuccess(FindCurrentPlaceResponse findCurrentPlaceResponse) {
                ArrayList<Restaurant> data = new ArrayList<>();
                    for(PlaceLikelihood placeLikelihood : findCurrentPlaceResponse.getPlaceLikelihoods()){
                        if(placeLikelihood.getPlace().getTypes().toString().contains("RESTAURANT")) {
                            data.add(new Restaurant(placeLikelihood.getPlace().getId(),
                                    placeLikelihood.getPlace().getName(),
                                    placeLikelihood.getPlace().getAddress(),
                                    placeLikelihood.getPlace().getTypes().toString(),
                                    "12H - 18H",1));
                                    //placeLikelihood.getPlace().getOpeningHours().toString(),
                                    //placeLikelihood.getPlace().getUserRatingsTotal()));

                        }
                    }
                mRecycler.setAdapter(new RestaurantAdapter(data));
                }
            };
    }


}
