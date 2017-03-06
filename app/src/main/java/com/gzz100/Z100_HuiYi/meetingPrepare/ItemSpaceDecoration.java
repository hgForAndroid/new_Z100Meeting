package com.gzz100.Z100_HuiYi.meetingPrepare;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * RecyclerView每个Item的间隔，可选横向或纵向。
 * Created by XieQXiong on 2016/9/23.
 */
public class ItemSpaceDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private int orientation;

    private int space;

    public ItemSpaceDecoration(int orientation, int space) {
        setOrientation(orientation);
        this.space = space;
    }

    private void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("invalid orientation:" + orientation);
        }
        this.orientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (this.orientation == HORIZONTAL){
            outRect.right = space;
        }else if (this.orientation == VERTICAL){
            outRect.bottom = space;
        }
    }

}
