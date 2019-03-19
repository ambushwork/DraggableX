package com.netatmo.ylu.library;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DraggableHelper {

    private static final String TAG = "DraggableHelper";
    private static final boolean LOCAL_LOG = true;
    private static final int LONG_PRESS_DELAY_SECONDS = 3 * 500;
    private static final int TOUCH_SLOP = 5;
    private static DraggableHelper Instance;
    private DraggableWrapperAdapter wrapperAdapter;
    private IDraggableAdapter draggableAdapter;

    @Nullable
    private DraggableInfo draggableInfo;
    private boolean isDragging = false;
    private int mLastX;
    private int mLastY;
    private RecyclerView mRecyclerView;
    @Nullable
    private RecyclerView.ViewHolder draggedViewHolder;
    private DraggingItemDecorator draggingItemDecorator;
    private int draggedPosition;


    private InternalHandler internalHandler = new InternalHandler(this);
    //should only react for long click event
    private RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handleActionDown(motionEvent);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int x = (int) (motionEvent.getX() + 0.5);
                    int y = (int) (motionEvent.getY() + 0.5);
                    if (isDragging) {
                        handleDraggingAction(motionEvent);
                        return true;
                    }
                    if (Math.abs(mLastX - x) > TOUCH_SLOP || Math.abs(mLastY - y) > TOUCH_SLOP) {
                        internalHandler.cancelLongPressDetection();
                    }
                    //stop the recycler from scrolling.
                    return isDragging;
                case MotionEvent.ACTION_UP:
                    if (isDragging) {
                        handleLongPressRelease();
                    } else {
                        internalHandler.cancelLongPressDetection();
                    }

                    break;

            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (isDragging) {
                        handleLongPressRelease();
                    } else {
                        internalHandler.cancelLongPressDetection();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.v(TAG, "onTouchEvent ACTION_MOVE");
                    if (isDragging) {
                        handleDraggingAction(motionEvent);
                    }
                    break;
            }
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    };

    private void handleActionDown(MotionEvent motionEvent) {
        mLastX = (int) (motionEvent.getX() + 0.5);
        mLastY = (int) (motionEvent.getY() + 0.5);
        internalHandler.startLongPressDetection(motionEvent, LONG_PRESS_DELAY_SECONDS);
    }

    @SuppressWarnings("unchecked")
    private void handleLongPressRelease() {
        isDragging = false;
        if (LOCAL_LOG) {
            Log.v(TAG, "handle long press release");
        }
        if (draggingItemDecorator != null) {
            draggingItemDecorator.finish();
        }
        wrapperAdapter.finishDragging();
        draggableAdapter.onReleased(draggedViewHolder, draggedPosition);
        draggedViewHolder = null;
    }

    private void handleLongPressAction(@NonNull MotionEvent event) {
        if (LOCAL_LOG) {
            Log.v(TAG, "handle long press action!");
        }
        isDragging = true;
        View view = mRecyclerView.findChildViewUnder(event.getX(), event.getY());
        if (view != null) {
            this.draggedViewHolder = mRecyclerView.getChildViewHolder(view);
            startDragging(event);
        }
    }

    private void handleDraggingAction(@NonNull MotionEvent event) {
        if (LOCAL_LOG) {
            Log.v(TAG, "handle dragging action");
        }
        final int touchX = (int) (event.getX() + 0.5f);
        final int touchY = (int) (event.getY() + 0.5f);

        mLastX = touchX;
        mLastY = touchY;


        draggingItemDecorator.refresh(mLastX, mLastY);

    }

    @SuppressWarnings("unchecked")
    private void startDragging(@NonNull MotionEvent event) {
        final int touchX = (int) (event.getX() + 0.5f);
        final int touchY = (int) (event.getY() + 0.5f);

        mLastX = touchX;
        mLastY = touchY;

        View view = mRecyclerView.findChildViewUnder(mLastX, mLastY);
        if (view != null) {
            draggedViewHolder = mRecyclerView.getChildViewHolder(view);
            draggedPosition = mRecyclerView.getChildAdapterPosition(view);
            wrapperAdapter.startDragging(draggedPosition);
            wrapperAdapter.onBindViewHolder(draggedViewHolder, draggedPosition);
            draggingItemDecorator = new DraggingItemDecorator(mRecyclerView, draggedViewHolder);
            draggingItemDecorator.start(mLastX, mLastY);
            draggableAdapter.onDragged(draggedViewHolder, draggedPosition);
        }

    }

    public void setDraggableInfo(@NonNull DraggableInfo draggableInfo) {
        this.draggableInfo = draggableInfo;
    }


    @SuppressWarnings("unchecked")
    public void attachRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        this.mRecyclerView = recyclerView;
        wrapperAdapter = new DraggableWrapperAdapter(recyclerView.getContext(), adapter);
        if (adapter instanceof IDraggableAdapter) {
            draggableAdapter = (IDraggableAdapter) adapter;
        }
        recyclerView.setAdapter(wrapperAdapter);
        recyclerView.addOnItemTouchListener(onItemTouchListener);
    }

    private static class InternalHandler extends Handler {
        private static final int MSG_LONG_PRESS = 1;
        private DraggableHelper helper;
        private MotionEvent currentMotionEvent;

        public InternalHandler(DraggableHelper helper) {
            this.helper = helper;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LONG_PRESS:
                    helper.handleLongPressAction(currentMotionEvent);
                    break;
            }
        }

        public void startLongPressDetection(MotionEvent event, int delay) {
            if (LOCAL_LOG) {
                Log.v(TAG, "Start long press detection");
            }
            this.currentMotionEvent = MotionEvent.obtain(event);
            sendEmptyMessageAtTime(MSG_LONG_PRESS, event.getDownTime() + delay);
        }

        public void cancelLongPressDetection() {
            if (LOCAL_LOG) {
                Log.v(TAG, "Cancel long press detection");
            }
            removeMessages(MSG_LONG_PRESS);
        }

    }
}
