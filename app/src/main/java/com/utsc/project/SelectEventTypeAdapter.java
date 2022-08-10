package com.utsc.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectEventTypeAdapter extends RecyclerView.Adapter<SelectEventTypeAdapter.MyViewHolder> {

    public ArrayList<String> eTypeList;
    public ArrayList<String> selectedTypes;
    private RecyclerView rv;

    public SelectEventTypeAdapter(ArrayList<String> eTypeList) {
        this.eTypeList = eTypeList;
        this.selectedTypes = new ArrayList<String>();
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_type_checkbox, parent, false);
        return new SelectEventTypeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String type = eTypeList.get(position);
        holder.checkBox.setText(type);

        if (selectedTypes.contains(type)) {
            holder.checkBox.setChecked(true);
        }
        else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()) {
                    if (!selectedTypes.contains(type)) {
                        selectedTypes.add(type);
                    }
                }
                else {
                    selectedTypes.remove(type);
                }
            }
        });

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
