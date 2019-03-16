package com.netatmo.ylu.library;

import android.support.v7.widget.RecyclerView;

public interface IDraggableAdapter<T extends RecyclerView.ViewHolder> {
    void onDragged(T viewHolder, int position);

    void onReleased(T viewHolder, int position);
}
