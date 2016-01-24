package com.kuo.urcoco.common;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Property;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * Created by User on 2015/11/30.
 */
public class ColorCircle extends ImageView {

    private static final int DEFAULT_COLOR = Color.parseColor("#FFA000");

    private float circleBackgroundRaduis = 0;

    private int color;

    private boolean first = true;

    private AnimatorSet set = new AnimatorSet();


    public ColorCircle(Context context) {
        super(context);

        color = DEFAULT_COLOR;
    }

    public ColorCircle(Context context, AttributeSet attrs) {
        super(context, attrs);

        color = DEFAULT_COLOR;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();

        paint.setColor(color);

        if(first) {
            if(getWidth() <= getHeight())
                circleBackgroundRaduis = getWidth() / 4;
            else
                circleBackgroundRaduis = getHeight() / 4;

            first = false;
        }


        canvas.drawCircle(getWidth() / 2, getHeight() / 2, circleBackgroundRaduis, paint);

    }

    public void setColor(int color) {
        this.color = color;
    }

    public void resetColor() {
        if(getWidth() <= getHeight())
            circleBackgroundRaduis = getWidth() / 4;
        else
            circleBackgroundRaduis = getHeight() / 4;

        invalidate();
    }

    public int getColor() {
        return color;
    }

    public void startAnimation(){

        set.playTogether(ObjectAnimator.ofFloat(this, radiusProperty, circleBackgroundRaduis, getWidth()));
        set.setDuration(800);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(circleBackgroundRaduis == getWidth()){
                    if(getWidth() <= getHeight())
                        circleBackgroundRaduis = getWidth();
                    else
                        circleBackgroundRaduis = getHeight();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.start();
    }

    public void cancelAnimation() {
        set.cancel();
        resetColor();
    }

    private Property<ColorCircle, Float> radiusProperty = new Property<ColorCircle, Float>(Float.class, "cricleBackgroundRaduis") {
        @Override
        public Float get(ColorCircle object) {
            return object.circleBackgroundRaduis;
        }

        @Override
        public void set(ColorCircle object, Float value) {
            object.circleBackgroundRaduis = value;
            invalidate();
        }
    };


}
