package com.kuo.cooldatepicker_library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.kuo.cooldatepicker_library.model.RectTextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by User on 2015/9/23.
 */
public class CalendarView extends View {

    private Paint paint, paintCircle;
    private Calendar calendar;
    private ArrayList<RectTextView> rectTextViews = new ArrayList<>();
    private OnClickListener onListener;
    private RectTextView rectTextViewOnClick;

    private static int textSize = 70;
    private static int weekPoint = 7;
    private static int textColor = Color.parseColor("#424242");
    private static int textMonthColor = Color.parseColor("#2800bcd4");
    private static int textFoucsColor = Color.parseColor("#FAFAFA");
    private static int circleFoucsColor = Color.parseColor("#00BCD4");

    private int year = 0;
    private int month = 0;
    private int day = 0;
    private int maxHeight = 0;

    private float x;
    private float y;

    private boolean onClick = false;

    public interface OnClickListener{
        void onClick(int year, int month, int day, String subText);
    }

    public CalendarView(Context context) {
        super(context);

        paint = new Paint();
        paint.setColor(textMonthColor);

        paintCircle = new Paint();
        paintCircle.setColor(circleFoucsColor);

    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(textMonthColor);

        paintCircle = new Paint();
        paintCircle.setColor(circleFoucsColor);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        rectTextViews.clear();

        int width = getWidth();
        float avgRectWidth = width / weekPoint;

        onDrawBackgroundOfYear(canvas);
        onDrawDayOfWeek(canvas);
        onDrawDayOfMonth(canvas, getLastDay());
        onDrawBackgroundOfMonth(canvas);

        if(onClick) {
            onClick = false;
            canvas.drawCircle(rectTextViewOnClick.getRect().centerX(), rectTextViewOnClick.getRect().centerY(), avgRectWidth/2, paintCircle);
            rectTextViewOnClick.setTextColor(textFoucsColor);
            rectTextViewOnClick.onDrawRectText(canvas, rectTextViewOnClick.getRect());
        }

    }

    /**
     * Private method.
     * */

    private void onDrawDayOfWeek(Canvas canvas) {

        int width = getWidth();
        float avgRectWidth = width / weekPoint;

        for(int i = 0 ; i < weekPoint ; i++) {
            Rect rect = new Rect((int)(i * avgRectWidth), (int)avgRectWidth, (int)avgRectWidth + (int)(i * avgRectWidth), (int)avgRectWidth*2);
            onDrawRectText(canvas, rect, getDayOfChinese(i), textColor, textSize, false);
        }

    }

    private void onDrawDayOfMonth(Canvas canvas, int maxDayOfMonth) {

        int rows = 0;
        int weekPoint = 7;
        int width = getWidth();
        int n = 0;
        float avgRectWidth = width / weekPoint;

        for(int i = 0 ; i < maxDayOfMonth ; i++) {
            if(i % weekPoint == 0){
                for(int j = 0 ; j < weekPoint ; j++){
                    if(rows == 0 && j >= getFirstDayOfWeek()){
                        n++;
                        Rect rect = new Rect((int)(j * avgRectWidth), ((int)avgRectWidth + (int)((rows + 1)*avgRectWidth)), (int)avgRectWidth + (int)(j * avgRectWidth), ((int)(avgRectWidth * 2)) + (int)((rows + 1)*avgRectWidth));
                        onDrawRectText(canvas, rect, "" + n, textColor, textSize, true);
                    } else if (rows != 0 && n < maxDayOfMonth) {
                        n++;
                        Rect rect = new Rect((int)(j * avgRectWidth), ((int)avgRectWidth + (int)((rows + 1)*avgRectWidth)), (int)avgRectWidth + (int)(j * avgRectWidth), ((int)(avgRectWidth * 2)) + (int)((rows + 1)*avgRectWidth));
                        onDrawRectText(canvas, rect, "" + n, textColor, textSize, true);
                    }

                }
                rows++;
            }
        }

        if(n != maxDayOfMonth){
            for(int i = 0 ; i <= maxDayOfMonth - n ; i++) {
                n++;
                Rect rect = new Rect((int)(i * avgRectWidth), ((int)avgRectWidth + (int)((rows + 1)*avgRectWidth)), (int)avgRectWidth + (int)(i * avgRectWidth), ((int)(avgRectWidth * 2)) + (int)((rows + 1)*avgRectWidth));
                onDrawRectText(canvas, rect, "" + n, textColor, textSize, true);
            }
            rows++;
        }

    }

    private void onDrawBackgroundOfMonth(Canvas canvas) {

        int width = getWidth();
        float avgRectWidth = width / weekPoint;

        Rect backgroundRect = new Rect(0, (int)avgRectWidth, width, rectTextViews.get(rectTextViews.size() - 1).getRect().bottom);

        onDrawRectText(canvas, backgroundRect, (calendar.get(Calendar.MONTH) + 1) + "", textMonthColor, backgroundRect.width()/2, false);

        setMaxHeight(backgroundRect.bottom);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, backgroundRect.bottom);
        this.setLayoutParams(layoutParams);
    }

    private void onDrawBackgroundOfYear(Canvas canvas) {

        int width = getWidth();
        float avgRectWidth = width / weekPoint;

        Rect backgroundRect = new Rect(0, 0, width, (int)avgRectWidth);

        onDrawRectText(canvas, backgroundRect, calendar.get(Calendar.YEAR) + "", textColor, textSize, false);

    }

    private void onDrawRectText(Canvas canvas, Rect rect, String text, int color, int size, boolean clickable) {

        RectTextView rectTextView = new RectTextView();
        rectTextView.setText(text);
        rectTextView.setTextSize(size);
        rectTextView.setTextColor(color);
        rectTextView.onDrawRectText(canvas, rect);

        if(clickable) {

            Calendar calendarSub = calendar;
            calendarSub.set(Calendar.DAY_OF_MONTH, Integer.valueOf(text));

            rectTextView.setSubText(getDayOfChinese(calendarSub.get(Calendar.DAY_OF_WEEK) - 1));
            rectTextView.setOnClickListener(onClickListener);

        }

        rectTextViews.add(rectTextView);

    }

    private String getDayOfChinese(int i) {
        String[] chineseDay = {"日", "一", "二", "三", "四", "五", "六"};
        return chineseDay[i];
    }

    /**
     * Public method of set.
     * */

    public void setOnClickListener(OnClickListener onClickListener) {
        onListener = onClickListener;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
    }

    public void setTextSize(int size) {
        textSize = size;
    }

    public void setTextColor(int color) {
        textColor = color;
    }

    public void setTextMonthColor(int color) {
        textMonthColor = color;
    }

    public void setTextFoucsColor(int color) {
        textFoucsColor = color;
    }

    public void setCircleFoucsColor(int color) {
        circleFoucsColor = color;
    }

    /**
     * Public method of get.
     * */

    private int getFirstDayOfWeek() {

        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.get(Calendar.DAY_OF_WEEK) - 1;

    }

    private int getLastDay() {

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        return calendar.get(Calendar.DAY_OF_MONTH);

    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                x = event.getX();
                y = event.getY();

                for(RectTextView rectTextView : rectTextViews) {
                    if(x > rectTextView.getRect().left && y > rectTextView.getRect().top
                            && x < rectTextView.getRect().right && y < rectTextView.getRect().bottom) {
                        rectTextView.setDown(true);
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:

                x = event.getX();
                y = event.getY();

                for(RectTextView rectTextView : rectTextViews) {

                    if(x > rectTextView.getRect().left && y > rectTextView.getRect().top
                        && x < rectTextView.getRect().right && y < rectTextView.getRect().bottom) {

                        rectTextView.setUp(true);
                        rectTextView.onTouch();

                    }
                }
                break;
        }
        return false;
    }

    private RectTextView.OnClickListener onClickListener = new RectTextView.OnClickListener() {
        @Override
        public void onClick(RectTextView rectTextView) {
            rectTextViewOnClick = rectTextView;
            day = Integer.valueOf(rectTextView.getText());
            rectTextView.setUp(false);
            rectTextView.setDown(false);
            onClick = true;
            x = 0;
            y = 0;
            invalidate();

            if(onListener != null){
                onListener.onClick(year, month, day, rectTextView.getSubText());
            }

        }
    };

}
