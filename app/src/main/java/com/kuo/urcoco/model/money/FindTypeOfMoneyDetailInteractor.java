package com.kuo.urcoco.model.money;

import android.content.Context;

import com.kuo.urcoco.view.OnFoundTypeOfMoneyDetailListener;

/*
 * Created by Kuo on 2016/1/5.
 */
public interface FindTypeOfMoneyDetailInteractor {
    void onFind(OnFoundTypeOfMoneyDetailListener onFoundTypeOfMoneyDetailListener, Context context, String moneyType, String typeName, String startDate, String endDate, int mDayCount);
}
