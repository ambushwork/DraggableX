package com.netatmo.ylu.library;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class DraggableWrapperAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private static final String TAG = "DraggableWrapperAdapter";
    private static final boolean LOCAL_LOG = true;
    private boolean isDragging = false;
    private int mDraggingItemInitialPosition = RecyclerView.NO_POSITION;

    private RecyclerView.Adapter<VH> adapter;

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

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

    public DraggableWrapperAdapter(@NonNull Context context, RecyclerView.Adapter<VH> adapter) {
        this.adapter = adapter;
        this.adapter.registerAdapterDataObserver(observer);
        // DraggableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return adapter.onCreateViewHolder(viewGroup, i);
    }

    @Override
    public void onBindViewHolder(@NonNull final VH viewHolder, final int i) {
        if (isDragging) {
            //todo
            if (mDraggingItemInitialPosition == i) {
                setFlags(viewHolder, IDraggableViewHolder.ACTIVE);
            } else {
                setFlags(viewHolder, IDraggableViewHolder.DRAGGING);
            }
            adapter.onBindViewHolder(viewHolder, i);
        } else {
            setFlags(viewHolder, IDraggableViewHolder.INIT);
            adapter.onBindViewHolder(viewHolder, i);
        }
    }

    private void setFlags(@NonNull final VH viewHolder, @IDraggableViewHolder.Flag int flags) {
        if (viewHolder instanceof IDraggableViewHolder) {
            if (((IDraggableViewHolder) viewHolder).getFlags() != flags) {
                ((IDraggableViewHolder) viewHolder).setUpdate(false);
            }
            ((IDraggableViewHolder) viewHolder).setFlags(flags);
        }
    }

    public void startDragging(int draggedPosition) {
        this.mDraggingItemInitialPosition = draggedPosition;
        this.isDragging = true;
    }

    public void finishDragging() {
        this.isDragging = false;
        this.mDraggingItemInitialPosition = RecyclerView.NO_POSITION;
    }

    @Override
    public int getItemCount() {
        return adapter.getItemCount();
    }
}
