package com.kuo.cooldatepicker_library.item;

import com.kuo.cooldatepicker_library.view.CalendarView;

import java.util.Calendar;

/**
 * Created by User on 2015/9/30.
 */
public class CalendarItem {

    private Calendar calendar;
    private CalendarView calendarView;

    public void setCalendarView(CalendarView calendarView) {
        this.calendarView = calendarView;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public CalendarView getCalendarView() {
        return calendarView;
    }
}
