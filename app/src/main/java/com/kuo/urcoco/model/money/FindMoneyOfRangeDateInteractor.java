package com.kuo.urcoco.model.money;

import android.content.Context;

import com.kuo.urcoco.view.OnFoundMoneyItemsListener;

/**
 * Created by User on 2015/12/27.
 */
public interface FindMoneyOfRangeDateInteractor {
    void findMoney(OnFoundMoneyItemsListener onFoundMoneyItemsListener, Context context, String startDate, String endDate);
}
