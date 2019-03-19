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
import com.netatmo.ylu.library.IDraggableViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>
        implements IDraggableAdapter<ExampleAdapter.ExampleViewHolder> {

    List<MockDataGenerator.MockItem> list = new ArrayList<>();
    private int draggedPosition = -1;
    private Context context;

    public void setList(List<MockDataGenerator.MockItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.example_recycler_view_item, viewGroup, false);
        return new ExampleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder viewHolder, int i) {
        MockDataGenerator.MockItem mockItem = list.get(i);
        viewHolder.id.setText(mockItem.getId());
        switch (viewHolder.getFlags()) {

            case IDraggableViewHolder.ACTIVE:
                viewHolder.itemView.setBackground(this.context.getDrawable(R.drawable.bg_item_dragging_state));
                break;
            case IDraggableViewHolder.DRAGGING:
                viewHolder.itemView.setBackground(this.context.getDrawable(R.drawable.bg_item_dragging_active_state));
                break;
            case IDraggableViewHolder.INIT:
                viewHolder.itemView.setBackground(this.context.getDrawable(R.drawable.bg_item_normal_state));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onDragged(ExampleViewHolder viewHolder, int position) {
        Log.v("EXAMPLE ADAPTER", "dragged");
        this.draggedPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public void onReleased(ExampleViewHolder viewHolder, int position) {
        Log.v("EXAMPLE ADAPTER", "released");
        this.draggedPosition = -1;
        notifyDataSetChanged();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder implements IDraggableViewHolder {

        TextView id;
        Button button;
        @Flag
        private int draggableState;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.example_recycler_view_item_id);
            button = itemView.findViewById(R.id.example_recycler_view_item_button);
        }

        @Override
        @Flag
        public int getFlags() {
            return draggableState;
        }

        @Override
        public void setFlags(@Flag int flags) {
            this.draggableState = flags;
        }
    }
}
