package com.netatmo.ylu.library;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;

public class DraggableHelper {

    private static final String TAG = "DraggableHelper";
    private static final boolean LOCAL_LOG = true;
    private static final int LONG_PRESS_DELAY_SECONDS = 2 * 1000;
    private static final int TOUCH_SLOP = 5;
    private static DraggableHelper Instance;
    private DraggableWrapperAdapter wrapperAdapter;
    @Nullable
    private DraggableInfo draggableInfo;
    private boolean isDragging = false;
    private int mLastX;
    private int mLastY;

    private InternalHandler internalHandler = new InternalHandler(this);
    //should only react for long click event
    private RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastX = (int) (motionEvent.getX() + 0.5);
                    mLastY = (int) (motionEvent.getY() + 0.5);
                    internalHandler.startLongPressDetection(motionEvent, LONG_PRESS_DELAY_SECONDS);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int x = (int) (motionEvent.getX() + 0.5);
                    int y = (int) (motionEvent.getY() + 0.5);
                    if (LOCAL_LOG) {
                        //Log.v(TAG, "ACTION_MOVE from X: " + mLastX + "->" + x + " Y: " + mLastY + "->" + y);
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
                case MotionEvent.ACTION_DOWN:
                    internalHandler.startLongPressDetection(motionEvent, LONG_PRESS_DELAY_SECONDS);
                    /*View view = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                    if (view != null) {
                        RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                    }*/
                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
            }
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    };

    public static DraggableHelper getInstance() {
        if (Instance == null) {
            Instance = new DraggableHelper();
        }
        return Instance;
    }

    private void handleLongPressRelease() {
        isDragging = false;
        if (LOCAL_LOG) {
            Log.v(TAG, "handle long press release");
        }
    }

    private void handleLongPressAction(@NonNull MotionEvent event) {
        if (LOCAL_LOG) {
            Log.v(TAG, "handle long press action!");
        }
        isDragging = true;
        //todo
    }

    private void onPreDrag(@NonNull final RecyclerView.ViewHolder viewHolder) {
        if (draggableInfo != null) {
            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getResources().getColor(draggableInfo.getSelectedMaskColor()));
        }
    }

    private void onPostDrag(@NonNull final RecyclerView.ViewHolder viewHolder) {
        if (draggableInfo != null) {
            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getResources().getColor(draggableInfo.getSelectedMaskColor()));
        }
    }

    public void setDraggableInfo(@NonNull DraggableInfo draggableInfo) {
        this.draggableInfo = draggableInfo;
    }


    @SuppressWarnings("unchecked")
    public void attachRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        wrapperAdapter = new DraggableWrapperAdapter(recyclerView.getContext(), adapter);
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
