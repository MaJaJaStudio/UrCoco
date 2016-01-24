package com.kuo.urcoco.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by User on 2015/11/5.
 */
public class WrapContentHeightGridView extends GridView {

    public WrapContentHeightGridView(Context context) {
        super(context);
    }

    public WrapContentHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int heightSpec;

        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            heightSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
        } else {
            heightSpec = heightMeasureSpec;
        }

        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
