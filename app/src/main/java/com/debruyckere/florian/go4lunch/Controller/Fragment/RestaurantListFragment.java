package com.debruyckere.florian.go4lunch.Controller.Fragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.debruyckere.florian.go4lunch.Model.FireBaseConnector;
import com.debruyckere.florian.go4lunch.Model.Restaurant;
import com.debruyckere.florian.go4lunch.Model.RestaurantAdapter;
import com.debruyckere.florian.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;


public class RestaurantListFragment extends BaseFragment {

    private RecyclerView mRecycler;
    private Context mContext;
    private Location mUserLocation;
    private ArrayList<Restaurant> mData;

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
        return new RestaurantListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() != null)
        mContext = getActivity().getApplicationContext();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        FireBaseConnector fBC = new FireBaseConnector();
        fBC.getRestaurantsData(getListener(),mContext);
        fBC.getUserLocation(mContext,getDistanceListener());


        mRecycler = view.findViewById(R.id.list_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        toolbarConfiguration(view);


        return view;
    }

    private void toolbarConfiguration(View view){
        Toolbar toolbar = view.findViewById(R.id.fragment_toolbar);

        if((getActivity() != null)) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            if (((AppCompatActivity) getActivity()).getSupportActionBar() != null)
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("restaurant List");
        }
        mMaterialSearchView = view.findViewById(R.id.fragment_search);
        mMaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){
                    ArrayList<Restaurant> found = new ArrayList<>();
                    for(Restaurant restaurant : mData){
                        if(restaurant.getName().contains(newText))
                            found.add(restaurant);
                    }
                    mRecycler.setAdapter(new RestaurantAdapter(found,mContext));
                }else
                    mRecycler.setAdapter(new RestaurantAdapter(mData,mContext));

                return true;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getActivity() != null)
        getActivity().getMenuInflater().inflate(R.menu.actionbar_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mMaterialSearchView.setMenuItem(item);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public OnCompleteListener getListener() {
        return new OnCompleteListener<FindCurrentPlaceResponse>(){
            @Override
            public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                if(task.isSuccessful() && task.getResult() != null) {
                    ArrayList<Restaurant> data = new ArrayList<>();

                    for(PlaceLikelihood placeLikelihood : task.getResult().getPlaceLikelihoods()){
                        if(placeLikelihood.getPlace().getTypes()!= null &&
                                placeLikelihood.getPlace().getTypes().toString().contains("RESTAURANT")) {

                            //----------------------------
                            //Get restaurant rate from Api
                            int rate;
                            if(placeLikelihood.getPlace().getUserRatingsTotal() != null){           //to avoid nullPointerException
                                rate = Math.round(placeLikelihood.getPlace().getUserRatingsTotal());
                            }else {
                                rate = 0;
                            }

                            final Restaurant restaurant = new Restaurant(placeLikelihood.getPlace().getId(),
                                    placeLikelihood.getPlace().getName(),
                                    placeLikelihood.getPlace().getAddress(),
                                    placeLikelihood.getPlace().getTypes().get(0).toString(),
                                    "24H/24",
                                    rate
                            );

                            //-----------------------------------
                            //Get Distance between restaurant and user
                            Location restaurantLocation = new Location("Restaurant");
                            if(placeLikelihood.getPlace().getLatLng()!= null) {
                                restaurantLocation.setLatitude(placeLikelihood.getPlace().getLatLng().latitude);
                                restaurantLocation.setLongitude(placeLikelihood.getPlace().getLatLng().longitude);
                            }

                            restaurant.setDistance(Math.round(mUserLocation.distanceTo(restaurantLocation)));

                            data.add(restaurant);
                        }
                    }
                    //---------------------
                    //Set Adapter with Data
                    mData = data;
                    mRecycler.setAdapter(new RestaurantAdapter(data,mContext));
                }
                else{
                    if(task.getException()!=null)
                    Log.e("List Listener",task.getException().toString());
                }
                }
            };
        }

    public OnCompleteListener getDistanceListener(){
        return  new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    mUserLocation = task.getResult();
                }
            }
        };
    }
}