package com.netatmo.ylu.library;

import android.support.v7.widget.RecyclerView;

public class DraggableHelper {

    private static DraggableHelper Instance;
    private DraggableWrapperAdapter wrapperAdapter;

    public static DraggableHelper getInstance() {
        if (Instance == null) {
            Instance = new DraggableHelper();
        }
        return Instance;
    }

    @SuppressWarnings("unchecked")
    public void attachRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        wrapperAdapter = new DraggableWrapperAdapter(adapter);
        recyclerView.setAdapter(wrapperAdapter);
    }
}
