package com.kuo.urcoco.model.days;

import com.kuo.urcoco.view.OnFoundDateListener;

/**
 * Created by Kuo on 2015/12/11.
 */
public interface FindDaysInteractor {
    void findDays(OnFoundDateListener onFoundDateListener, int year, int month, int day, int times);
}
