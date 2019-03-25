package com.netatmo.ylu.library;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface IDraggableViewHolder {

    int INIT = 1;
    int ACTIVE = 2;
    int DRAGGING = 3;

    boolean isUpdated();

    void setUpdate(boolean updated);

    @Flag
    int getFlags();

    void setFlags(@Flag int flags);

    @IntDef({INIT, ACTIVE, DRAGGING})
    @Retention(RetentionPolicy.SOURCE)
    @interface Flag {
    }
}
