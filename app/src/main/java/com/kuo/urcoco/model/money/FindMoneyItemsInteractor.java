package com.kuo.urcoco.model.money;

import android.content.Context;

import com.kuo.urcoco.view.OnFoundMoneyItemsListener;

/*
 * Created by Kuo on 2015/12/11.
 */
public interface FindMoneyItemsInteractor {
    void findMoneyOfDay(OnFoundMoneyItemsListener onFoundMoneyItemsListener, Context context, String date);
}
