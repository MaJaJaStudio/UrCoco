package com.kuo.urcoco.model.money;

import android.content.Context;

import com.kuo.urcoco.view.OnFoundIntegerListener;

/**
 * Created by Kuo on 2015/12/11.
 */
public interface FindCostOfMonthInteractor {
    void findInteger(OnFoundIntegerListener onFoundIntegerListener, Context context, int year, int month);
}
