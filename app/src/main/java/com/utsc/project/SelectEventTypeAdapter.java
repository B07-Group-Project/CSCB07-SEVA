package com.utsc.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectEventTypeAdapter extends RecyclerView.Adapter<SelectEventTypeAdapter.MyViewHolder> {

    private ArrayList<String> eTypeList;
    private RecyclerView rv;

    public SelectEventTypeAdapter(ArrayList<String> eTypeList) {
        this.eTypeList = eTypeList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;

        public MyViewHolder(final View view) {
            super(view);
            this.checkBox = view.findViewById(R.id.eventTypeCheckBox);
        }
    }

    @NonNull
    @Override
    public SelectEventTypeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_my_events, parent, false);
        return new SelectEventTypeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String type = eTypeList.get(position);
        holder.checkBox.setText(type);
    }

    @Override
    public int getItemCount() {
        if (eTypeList != null) {
            return eTypeList.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView r) {
        rv = r;
    }
}
