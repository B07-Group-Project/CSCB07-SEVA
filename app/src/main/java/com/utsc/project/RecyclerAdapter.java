package com.utsc.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<Event> eventsList;

    public RecyclerAdapter(ArrayList<Event> myEvents) {
        this.eventsList = myEvents;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView eventName;

        public MyViewHolder(final View view) {
            super(view);
            this.eventName = view.findViewById(R.id.textView);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_my_events, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        //String name = eventsList.get(position).getName();
        //holder.eventName.setText(name);
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}
