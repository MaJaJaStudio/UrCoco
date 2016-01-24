package com.kuo.cooldatepicker_library.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by User on 2015/9/24.
 */
public class RectTextView {

    private Paint paint;
    private Rect rect;

    private String text;
    private String subText;

    private float x;
    private float y;

    private int textSize = 70;
    private int textColor = 0;

    private boolean isDown = false;
    private boolean isUp = false;

    private OnClickListener onClickListener;

    public interface OnClickListener {
        void onClick(RectTextView rectTextView);
    }

    public void onDrawRectText(Canvas canvas, Rect rect) {

        Rect textBounds = new Rect();

        paint = new Paint();
        paint.setStrokeWidth(textSize);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.getTextBounds(text, 0, text.length(), textBounds);

        canvas.drawText(text, rect.centerX(), rect.centerY() - textBounds.exactCenterY(), paint);

        this.rect = rect;
        this.x = rect.centerX();
        this.y = rect.centerY() - textBounds.exactCenterY();

    }

    public void onTouch() {
        if(onClickListener != null) {
            if(isDown && isUp) {
                onClickListener.onClick(this);
            }
        }
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setDown(boolean i) {
        isDown = i;
    }

    public void setUp(boolean i) {
        isUp = i;
    }

    public Rect getRect() {
        return rect;
    }

    public String getSubText() {
        return subText;
    }

    public String getText() {
        return text;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getTextSize() {
        return textSize;
    }

}
