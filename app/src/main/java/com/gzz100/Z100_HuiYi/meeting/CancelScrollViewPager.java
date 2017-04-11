package com.gzz100.Z100_HuiYi.meeting;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author XieQingXiong
 * @description
 * @packageName com.gzz100.Z100_HuiYi.meeting
 * @className
 * @time 2017/4/5   14:33
 */

public class CancelScrollViewPager extends ViewPager {
    private boolean cancelScroll = true;
    public CancelScrollViewPager(Context context) {
        super(context);
    }

    public CancelScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 控件实例化后调用该方法，使控件的滑动失效。默认是可以滑动的。
     * @param cancelScroll
     */
    public void setCancelScroll(boolean cancelScroll) {
        this.cancelScroll = cancelScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (cancelScroll){
            return super.onTouchEvent(ev);
        }else {
            return false;
        }
    }
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        if (cancelScroll) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }

    }

}
