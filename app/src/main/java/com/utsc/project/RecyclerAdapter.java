package com.utsc.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<Event> eventsList;

    public RecyclerAdapter(ArrayList<Event> myEvents) {
        this.eventsList = myEvents;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView eventName, creator, date, time, description;
        private ToggleButton join_button;
        private ImageView image;

        public MyViewHolder(final View view) {
            super(view);
            this.eventName = view.findViewById(R.id.eventNameTextView);
            this.creator = view.findViewById(R.id.creatorTextView);
            this.date = view.findViewById(R.id.dateTextView);
            this.time = view.findViewById(R.id.timeTextView);
            this.description = view.findViewById(R.id.descriptionTextView);
            this.join_button = view.findViewById(R.id.joinToggleButton);
            this.image = view.findViewById(R.id.eventImageView);
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
        holder.eventName.setText(eventsList.get(position).getName());
        holder.creator.setText("Created by: " + eventsList.get(position).getCreator());
        holder.date.setText(eventsList.get(position).getDate());
        holder.time.setText(eventsList.get(position).getStartTime() + " to " + eventsList.get(position).getEndTime());
        holder.description.setText(eventsList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }


}
