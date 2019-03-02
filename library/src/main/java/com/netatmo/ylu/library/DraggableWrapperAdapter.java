package com.netatmo.ylu.library;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class DraggableWrapperAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private static final boolean LOCAL_LOG = true;

    private RecyclerView.Adapter<VH> adapter;

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {

        }
    };

    public DraggableWrapperAdapter(RecyclerView.Adapter<VH> adapter) {
        this.adapter = adapter;
        this.adapter.registerAdapterDataObserver(observer);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return adapter.onCreateViewHolder(viewGroup, i);
    }

    @Override
    public void onBindViewHolder(@NonNull VH viewHolder, int i) {
        adapter.onBindViewHolder(viewHolder, i);
    }

    @Override
    public int getItemCount() {
        return adapter.getItemCount();
    }
}
