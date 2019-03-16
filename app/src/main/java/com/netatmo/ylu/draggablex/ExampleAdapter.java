package com.netatmo.ylu.draggablex;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.netatmo.ylu.library.IDraggableAdapter;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements IDraggableAdapter<RecyclerView.ViewHolder> {

    List<MockDataGenerator.MockItem> list = new ArrayList<>();
    private int draggedPosition = -1;
    private Context context;

    public void setList(List<MockDataGenerator.MockItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.example_recycler_view_item, viewGroup, false);
        return new ExampleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MockDataGenerator.MockItem mockItem = list.get(i);
        ((ExampleViewHolder) viewHolder).id.setText(mockItem.getId());
        if (draggedPosition == i) {
            ((ExampleViewHolder) viewHolder).itemView.setBackgroundColor(this.context.getResources().getColor(R.color.colorAccent));
        } else {
            ((ExampleViewHolder) viewHolder).itemView.setBackgroundColor(this.context.getResources().getColor(R.color.transparent));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onDragged(RecyclerView.ViewHolder viewHolder, int position) {
        Log.v("EXAMPLE ADAPTER", "dragged");
        this.draggedPosition = position;
        notifyItemChanged(position);
    }

    @Override
    public void onReleased(RecyclerView.ViewHolder viewHolder, int position) {
        Log.v("EXAMPLE ADAPTER", "released");
        this.draggedPosition = -1;
        notifyItemChanged(position);
    }

    private class ExampleViewHolder extends RecyclerView.ViewHolder {

        TextView id;
        Button button;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.example_recycler_view_item_id);
            button = itemView.findViewById(R.id.example_recycler_view_item_button);
        }
    }
}
