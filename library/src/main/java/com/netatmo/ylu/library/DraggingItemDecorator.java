package com.netatmo.ylu.library;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DraggingItemDecorator extends RecyclerView.ItemDecoration {

    private static final int SCALE = 1;
    protected final RecyclerView mRecyclerView;
    protected RecyclerView.ViewHolder mDraggingItemViewHolder;
    private Bitmap mDraggingItemImage;
    private Paint mPaint;
    private int mLastX;
    private int mLastY;
    private int initialX;
    private int initialY;

    public DraggingItemDecorator(RecyclerView mRecyclerView, RecyclerView.ViewHolder mDraggingItemViewHolder) {
        this.mRecyclerView = mRecyclerView;
        this.mDraggingItemViewHolder = mDraggingItemViewHolder;
        mPaint = new Paint();
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int h = c.getHeight();
        int w = c.getWidth();
        int decorationH = h * SCALE;
        int decorationW = w * SCALE;

        int translateX = mDraggingItemViewHolder.itemView.getLeft() + (mLastX - initialX);
        int translateY = mDraggingItemViewHolder.itemView.getTop() + (mLastY - initialY);
        c.translate(translateX, translateY);
        int saveCount = c.save();
        c.scale(1, SCALE);

        c.drawBitmap(mDraggingItemImage, 0, 0, mPaint);
        //mDraggingItemViewHolder.itemView.draw(c);
        c.restoreToCount(saveCount);
    }

    public void refresh(int touchX, int touchY) {
        mLastX = touchX;
        mLastY = touchY;
        mDraggingItemImage = createDraggingItemImage(mDraggingItemViewHolder.itemView);
        mRecyclerView.postInvalidate();
    }

    public void start(int touchX, int touchY) {
        this.initialX = touchX;
        this.initialY = touchY;
        this.mLastX = touchX;
        this.mLastY = touchY;
        final View itemView = mDraggingItemViewHolder.itemView;
        mDraggingItemImage = createDraggingItemImage(itemView);
        itemView.setVisibility(View.INVISIBLE);
        mRecyclerView.addItemDecoration(this);
        //invalidate to trigger the onDrawOver method.
        mRecyclerView.postInvalidate();
    }

    public void finish() {
        mRecyclerView.removeItemDecoration(this);
        mDraggingItemViewHolder.itemView.setVisibility(View.VISIBLE);
        mRecyclerView.postInvalidate();
    }

    private Bitmap createDraggingItemImage(View v) {
        int viewTop = v.getTop();
        int viewLeft = v.getLeft();
        int viewWidth = v.getWidth();
        int viewHeight = v.getHeight();


        v.measure(
                View.MeasureSpec.makeMeasureSpec(viewWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(viewHeight, View.MeasureSpec.EXACTLY));

        v.layout(viewLeft, viewTop, viewLeft + viewWidth, viewTop + viewHeight);

        final Bitmap bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);

        final Canvas canvas = new Canvas(bitmap);

        final int savedCount = canvas.save();
        // NOTE: Explicitly set clipping rect. This is required on Gingerbread.
        v.draw(canvas);
        canvas.restoreToCount(savedCount);

        return bitmap;
    }
}
