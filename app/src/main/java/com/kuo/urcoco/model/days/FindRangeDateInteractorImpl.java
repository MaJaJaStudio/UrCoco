package com.kuo.urcoco.model.days;

import com.kuo.urcoco.view.OnFoundDateListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by User on 2015/12/26.
 */
public class FindRangeDateInteractorImpl implements FindRangeDateInteractor {

    @Override
    public void findRangeDate(OnFoundDateListener onFoundDateListener, String startDate, String endDate) {
        onFoundDateListener.onFoundDate(getDaysOfRange(startDate, endDate));
    }

    private ArrayList<String> getDaysOfRange(String startDate, String endDate) {

        ArrayList<String> dates  = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        Calendar computingCalendar = Calendar.getInstance();

        try {
            computingCalendar.setTime(simpleDateFormat.parse(startDate));
            startCalendar.setTime(simpleDateFormat.parse(startDate));
            endCalendar.setTime(simpleDateFormat.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int count = Math.abs(endCalendar.get(Calendar.DAY_OF_YEAR) - startCalendar.get(Calendar.DAY_OF_YEAR)) + (Math.abs(endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)) * 365);

        for(int i = 0; i < count; i++) {
            dates.add(computingCalendar.get(Calendar.YEAR) + "-" + (computingCalendar.get(Calendar.MONTH)+1) + "-" + computingCalendar.get(Calendar.DAY_OF_MONTH));
            computingCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dates;
    }
}
