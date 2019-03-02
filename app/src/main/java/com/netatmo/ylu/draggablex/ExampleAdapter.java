package com.netatmo.ylu.draggablex;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<MockDataGenerator.MockItem> list = new ArrayList<>();

    public void setList(List<MockDataGenerator.MockItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.example_recycler_view_item, viewGroup, false);
        return new ExampleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MockDataGenerator.MockItem mockItem = list.get(i);
        ((ExampleViewHolder) viewHolder).name.setText(mockItem.getName());
        ((ExampleViewHolder) viewHolder).id.setText(mockItem.getId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ExampleViewHolder extends RecyclerView.ViewHolder {

        TextView id;
        TextView name;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.example_recycler_view_item_id);
            name = itemView.findViewById(R.id.example_recycler_view_item_name);
        }
    }
}
