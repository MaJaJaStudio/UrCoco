package com.kuo.urcoco.model.days;

import com.kuo.urcoco.view.OnFoundDateListener;

import java.util.ArrayList;
import java.util.Calendar;

/*
 * Created by Kuo on 2015/12/11.
 */
public class FindDaysInteractorImpl implements FindDaysInteractor {


    @Override
    public void findDays(OnFoundDateListener onFoundDateListener, int year, int month, int day, int times) {
        switch (times) {
            case 0:
                onFoundDateListener.onFoundDate(getDaysOfDay(year, month, day));
                break;
            case 1:
                onFoundDateListener.onFoundDate(getDaysOfWeek());
                break;
            case 2:
                onFoundDateListener.onFoundDate(getDaysOfMonth(year, month));
                break;
        }
    }

    private ArrayList<String> getDaysOfDay(int year, int month, int day) {

        ArrayList<String> date = new ArrayList<>();
        date.add(year + "-" + month + "-" + day);

        return date;
    }

    private ArrayList<String> getDaysOfWeek() {

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        ArrayList<String> date = new ArrayList<>();

        for(int i = 0 ; i < 7 ; i++) {
            date.add(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return date;
    }

    private ArrayList<String> getDaysOfMonth(int year, int month) {

        ArrayList<String> date = new ArrayList<>();

        for(int j = 0 ; j < getLastDayOfMonth(year, month) ; j++) {
            date.add(year + "-" + month + "-" + (j+1));
        }
        return date;
    }

    private int getLastDayOfMonth(int year, int month) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
