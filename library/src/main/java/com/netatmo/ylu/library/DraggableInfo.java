package com.netatmo.ylu.library;

import android.support.annotation.ColorRes;

public class DraggableInfo {

    @ColorRes
    private int selectedMaskColor;

    @ColorRes
    private int unselectedMaskColor;

    public DraggableInfo(@ColorRes int selectedMaskColor, @ColorRes int unselectedMaskColor) {
        this.selectedMaskColor = selectedMaskColor;
        this.unselectedMaskColor = unselectedMaskColor;
    }

    @ColorRes
    public int getSelectedMaskColor() {
        return selectedMaskColor;
    }

    public int getUnselectedMaskColor() {
        return unselectedMaskColor;
    }
}
