package com.kuo.urcoco.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/*
 * Created by Kuo on 2015/11/23.
 */
public class CircleTextView extends ImageView {

    private String text = "No String.";
    private int textColor = Color.parseColor("#FFFFFF");
    private int textSize = 20;
    private int color = Color.parseColor("#00BCD4");
    private int colorDark = Color.parseColor("#00BCD4");

    public CircleTextView(Context context) {
        super(context);
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();

        //.setColor(colorDark);

        //canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2, paint);

        paint.setColor(color);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2, paint);


        Paint textPaint = new Paint();
        textPaint.setTextSize((int) dpToPx(getContext(), (float)textSize));
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);

        int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
        int xPos = (canvas.getWidth() / 2);

        canvas.drawText(text, xPos, yPos, textPaint);

    }

    public void setCircleColor(int color) {
        this.color = color;
        invalidate();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public void setTextColor(int color) {
        this.textColor = color;
        invalidate();
    }

    public void setTextSize(int size) {
        this.textSize = size;
        invalidate();
    }

    public int getColor() {
        return color;
    }

    public static float dpToPx(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

}
