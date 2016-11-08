package com.gzz100.Z100_HuiYi.meeting;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by XieQXiong on 2016/11/3.
 */

public class MyViewPager extends ViewPager {
    private boolean scrollble = true;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if (!scrollble) {
//            return true;
//        }
//        return super.onTouchEvent(ev);
//    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (!scrollble) {
            return true;
        }
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (!scrollble) {
//            return true;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
}
