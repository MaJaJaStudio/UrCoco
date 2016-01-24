package com.kuo.urcoco.model.days;

import com.kuo.urcoco.view.OnFoundDateListener;

/**
 * Created by User on 2015/12/26.
 */
public interface FindRangeDateInteractor {
    void findRangeDate(OnFoundDateListener onFoundDateListener, String startDate, String endDate);
}
