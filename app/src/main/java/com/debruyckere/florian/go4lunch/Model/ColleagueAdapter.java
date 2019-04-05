package com.debruyckere.florian.go4lunch.Model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.debruyckere.florian.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Created by Debruyckère Florian on 02/01/2019.
 */
public class ColleagueAdapter extends RecyclerView.Adapter<ColleagueAdapter.ColleagueViewHolder> {

    private ArrayList<Colleague> mData;

    public OnCompleteListener getCompleteListener(){
        OnCompleteListener<QuerySnapshot> listener = new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document :task.getResult()){
                        Colleague colleague = new Colleague(document.getId()
                                ,(String) document.getData().get("name")
                                ,(String) document.getData().get("surname"));

                        mData.add(colleague);
                    }
                }else{
                    Log.w("Database error : ",task.getException());
                }
            }
        };

        return listener;
    }

    public ColleagueAdapter(ArrayList<Colleague> pData){
        //new FireBaseConnector().getColleague(getCompleteListener());
        mData = pData;
    }

    @NonNull
    @Override
    public ColleagueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.colleague_cell,parent,false);


        return new ColleagueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColleagueViewHolder holder, int position) {

        holder.display(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ColleagueViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;

        private ColleagueViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.colleague_cell_text);
        }

        private void display(Colleague param){
            mTextView.setText(new StringBuilder(param.getName()+" "+ param.getSurname()));
        }
    }
}
