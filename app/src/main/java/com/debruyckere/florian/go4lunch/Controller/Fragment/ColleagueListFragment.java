package com.debruyckere.florian.go4lunch.Controller.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debruyckere.florian.go4lunch.Model.Colleague;
import com.debruyckere.florian.go4lunch.Model.ColleagueAdapter;
import com.debruyckere.florian.go4lunch.Model.FireBaseConnector;
import com.debruyckere.florian.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ColleagueListFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private Context mContext;

    public ColleagueListFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ColleagueListFragment.
     */

    public static ColleagueListFragment newInstance() {
        return new ColleagueListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_colleague_list, container, false);

        if(getActivity() != null)
            mContext = getActivity().getApplicationContext();

        new FireBaseConnector().getColleague(getCompleteListener());

        mRecyclerView = view.findViewById(R.id.colleague_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));


        return view;
    }

    public OnCompleteListener getCompleteListener(){
        return new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult() != null){
                    ArrayList<Colleague> mData = new ArrayList<>();
                    for(QueryDocumentSnapshot document :task.getResult()){
                        Colleague colleague = new Colleague(document.getId()
                                ,(String) document.getData().get("name")
                                ,(String) document.getData().get("surname"));

                        mData.add(colleague);
                        mRecyclerView.setAdapter(new ColleagueAdapter(mData, mContext));
                    }
                }else{
                    Log.w("Database error : ",task.getException());
                }
            }
        };
    }
}
