package com.kuo.cooldatepicker_library.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuo.cooldatepicker_library.R;
import com.kuo.cooldatepicker_library.adapter.RecyclerViewAdapter;
import com.kuo.cooldatepicker_library.item.CalendarItem;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by User on 2015/10/4.
 */
public class CoolDatePicker extends LinearLayout {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<CalendarItem> calendarItems = new ArrayList<>();

    private LinearLayout titleLinearLayout, titleTextLinearLayout;
    private TextView textMonth, textYear, textDay, textDayOfWeek;

    private OnDateChangedListener onDateChangedListener;

    private static int calendarViewTextColor = Color.parseColor("#424242");
    private static int calendarViewTextMonthColor = Color.parseColor("#2800bcd4");
    private static int calendarViewTextFoucsColor = Color.parseColor("#FAFAFA");
    private static int calendarViewCircleFoucsColor = Color.parseColor("#00BCD4");
    private static int calendarViewBackground = Color.parseColor("#0000E4FF");

    private static int titleTextColor = Color.parseColor("#FAFAFA");
    private static int titleBackgroundColor = Color.parseColor("#00BCD4");

    private Calendar calendar = Calendar.getInstance();

    public interface OnDateChangedListener {
        void onDateChanged(int year, int month, int day, String subText);
    }

    public CoolDatePicker(Context context) {
        this(context, null);
    }

    public CoolDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        initPicker(context, attrs);
    }


    private void initPicker(Context context, AttributeSet attrs) {

        this.setOrientation(VERTICAL);
        onAddTitleLayout(context, attrs);
        initCalendarViewStylealbe(context, attrs);
        onAddRecyclerView();

    }

    private void initCalendarViewStylealbe(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CoolDatePicker);

        calendarViewTextColor = typedArray.getColor(R.styleable.CoolDatePicker_calendar_textColor, Color.parseColor("#424242"));
        calendarViewTextMonthColor = typedArray.getColor(R.styleable.CoolDatePicker_calendar_textMonthColor, Color.parseColor("#2800bcd4"));
        calendarViewTextFoucsColor = typedArray.getColor(R.styleable.CoolDatePicker_calendar_textFoucsColor, Color.parseColor("#FAFAFA"));
        calendarViewCircleFoucsColor = typedArray.getColor(R.styleable.CoolDatePicker_calendar_circleFoucsColor, Color.parseColor("#00BCD4"));
        calendarViewBackground = typedArray.getColor(R.styleable.CoolDatePicker_calendar_backgroundColor, Color.parseColor("#0000E4FF"));

    }

    private void onAddRecyclerView() {

        ViewGroup.LayoutParams recyclerLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutParams(recyclerLayoutParams);

        linearLayoutManager = new LinearLayoutManager(getContext());

        onInitCalendarItem();

        this.addView(recyclerView);
    }

    private void onAddTitleLayout(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CoolDatePicker);

        LinearLayout.LayoutParams titleLinearLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int titleTextMonthScaleSize = getResources().getDimensionPixelSize(R.dimen.title_text_month);
        int titleTextDayScaleSize = getResources().getDimensionPixelSize(R.dimen.title_text_day);
        int titleTextDayOfWeekScaleSize = getResources().getDimensionPixelSize(R.dimen.title_text_day_of_week);
        int titleTextYearScaleSize = getResources().getDimensionPixelSize(R.dimen.title_text_year);

        titleTextLinearLayout = new LinearLayout(getContext());
        titleLinearLayout = new LinearLayout(getContext());
        textYear = new TextView(getContext());
        textMonth = new TextView(getContext());
        textDay = new TextView(getContext());
        textDayOfWeek = new TextView(getContext());

        titleTextLinearLayout.setLayoutParams(titleLinearLayoutParams);
        titleTextLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        titleTextLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        textDay.setLayoutParams(textLayoutParams);
        //textDay.setTextSize(typedArray.getInteger(R.styleable.CoolDatePicker_title_textDaySize, 60));
        textDay.setTextSize(titleTextDayScaleSize);
        textDay.setText("4");
        textDay.setTextColor(typedArray.getColor(R.styleable.CoolDatePicker_title_textDayColor, titleTextColor));

        textDayOfWeek.setLayoutParams(textLayoutParams);
        //textDayOfWeek.setTextSize(typedArray.getInteger(R.styleable.CoolDatePicker_title_textDayOfWeekSize, 30));
        textDayOfWeek.setTextSize(titleTextDayOfWeekScaleSize);
        textDayOfWeek.setText("星期日");
        textDayOfWeek.setTextColor(typedArray.getColor(R.styleable.CoolDatePicker_title_textDayOfWeekColor, titleTextColor));

        titleTextLinearLayout.addView(textDay);
        titleTextLinearLayout.addView(textDayOfWeek);

        titleLinearLayout.setLayoutParams(titleLinearLayoutParams);
        titleLinearLayout.setOrientation(LinearLayout.VERTICAL);
        titleLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        textMonth.setLayoutParams(textLayoutParams);
        //textMonth.setTextSize(typedArray.getInteger(R.styleable.CoolDatePicker_title_textMonthSize, 40));
        textMonth.setTextSize(titleTextMonthScaleSize);
        textMonth.setText("十月");
        textMonth.setTextColor(typedArray.getColor(R.styleable.CoolDatePicker_title_textMonthColor, titleTextColor));

        textYear.setLayoutParams(textLayoutParams);
        //textYear.setTextSize(typedArray.getInteger(R.styleable.CoolDatePicker_title_textYearSize, 40));
        textYear.setTextSize(titleTextYearScaleSize);
        textYear.setText("2015");
        textYear.setTextColor(typedArray.getColor(R.styleable.CoolDatePicker_title_textYearColor, titleTextColor));

        titleLinearLayout.setBackgroundColor(titleBackgroundColor);
        titleLinearLayout.addView(textMonth);
        titleLinearLayout.addView(titleTextLinearLayout);
        titleLinearLayout.addView(textYear);

        this.addView(titleLinearLayout);
    }

    private void onInitCalendarItem() {

        int calendarTextScaleSize = getResources().getDimensionPixelSize(R.dimen.calendar_text_size);

        for(int i = 0 ; i < 12 ; i++) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR), (i+1), 0);

            CalendarView calendarView = new CalendarView(getContext());
            calendarView.setCalendar(calendar);
            calendarView.setBackgroundColor(calendarViewBackground);
            calendarView.setTextSize(calendarTextScaleSize);
            calendarView.setTextColor(calendarViewTextColor);
            calendarView.setTextFoucsColor(calendarViewTextFoucsColor);
            calendarView.setTextMonthColor(calendarViewTextMonthColor);
            calendarView.setCircleFoucsColor(calendarViewCircleFoucsColor);

            CalendarItem calendarItem = new CalendarItem();
            calendarItem.setCalendar(calendar);
            calendarItem.setCalendarView(calendarView);
            calendarItems.add(calendarItem);

        }

        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), calendarItems);
        recyclerViewAdapter.setOnDateChangeListener(onDateChangeListener);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addOnScrollListener(onScrollListener);

        Calendar calendar = Calendar.getInstance();
        recyclerView.scrollToPosition(calendar.get(Calendar.MONTH));

    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if(linearLayoutManager.findFirstVisibleItemPosition() == 4 && dy > -100) {
                insterLastCalendarItem(lastYearState - 1);
            } else if(linearLayoutManager.findLastVisibleItemPosition() == recyclerViewAdapter.getItemCount() - 2 && dy > 100) {
                insterNewCalendarItem(newYearState + 1);
            }

        }
    };

    private int lastYearState = 2015;
    private int newYearState = 2015;

    private void insterLastCalendarItem(int year) {

        int calendarTextScaleSize = getResources().getDimensionPixelSize(R.dimen.calendar_text_size);

        for(int i = 0 ; i < 12 ; i++) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, (i+1), 0);

            CalendarView calendarView = new CalendarView(getContext());
            calendarView.setCalendar(calendar);
            calendarView.setBackgroundColor(calendarViewBackground);
            calendarView.setTextSize(calendarTextScaleSize);
            calendarView.setTextColor(calendarViewTextColor);
            calendarView.setTextFoucsColor(calendarViewTextFoucsColor);
            calendarView.setTextMonthColor(calendarViewTextMonthColor);
            calendarView.setCircleFoucsColor(calendarViewCircleFoucsColor);

            CalendarItem calendarItem = new CalendarItem();
            calendarItem.setCalendar(calendar);
            calendarItem.setCalendarView(calendarView);
            recyclerViewAdapter.insterCalendarItem(i, calendarItem);

            if(lastYearState != calendar.get(Calendar.YEAR)) {
                lastYearState = calendar.get(Calendar.YEAR);
            }
        }

    }

    private void insterNewCalendarItem(int year) {

        int calendarTextScaleSize = getResources().getDimensionPixelSize(R.dimen.calendar_text_size);

        for(int i = 0 ; i < 12 ; i++) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, (i+1), 0);

            CalendarView calendarView = new CalendarView(getContext());
            calendarView.setCalendar(calendar);
            calendarView.setBackgroundColor(calendarViewBackground);
            calendarView.setTextSize(calendarTextScaleSize);
            calendarView.setTextColor(calendarViewTextColor);
            calendarView.setTextFoucsColor(calendarViewTextFoucsColor);
            calendarView.setTextMonthColor(calendarViewTextMonthColor);
            calendarView.setCircleFoucsColor(calendarViewCircleFoucsColor);

            CalendarItem calendarItem = new CalendarItem();
            calendarItem.setCalendar(calendar);
            calendarItem.setCalendarView(calendarView);
            recyclerViewAdapter.insterCalendarItem(calendarItem);

            if(newYearState != calendar.get(Calendar.YEAR)) {
                newYearState = calendar.get(Calendar.YEAR);
            }
        }
    }

    private RecyclerViewAdapter.OnDateChangeListener onDateChangeListener = new RecyclerViewAdapter.OnDateChangeListener() {
        @Override
        public void onDateChange(int year, int month, int day, String subText) {

            textYear.setText("" + year);
            textMonth.setText(getChineseOfMonth(month) + "月");
            textDay.setText("" + day);
            textDayOfWeek.setText("星期" + subText);

            if(onDateChangedListener != null) {
                onDateChangedListener.onDateChanged(year, month, day, subText);
            }

        }
    };

    private String getChineseOfMonth(int month) {

        String[] chineseOfMonth = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};

        return chineseOfMonth[month - 1];
    }

    public void setOnDateChangedListener(OnDateChangedListener onDateChangedListener) {
        this.onDateChangedListener = onDateChangedListener;
    }

}
