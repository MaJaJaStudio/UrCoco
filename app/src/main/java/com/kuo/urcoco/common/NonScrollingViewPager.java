package com.kuo.urcoco.common;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Kuo on 2016/1/28.
 */
public class NonScrollingViewPager extends ViewPager {

    private boolean isScrollingEnabled;

    public NonScrollingViewPager(Context context) {
        super(context);
    }

    public NonScrollingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.isScrollingEnabled) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.isScrollingEnabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    public void setIsScrollingEnabled(boolean isScrollingEnabled) {
        this.isScrollingEnabled = isScrollingEnabled;
    }
}
