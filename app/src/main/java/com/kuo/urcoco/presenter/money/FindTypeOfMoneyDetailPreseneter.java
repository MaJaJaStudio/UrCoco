package com.kuo.urcoco.presenter.money;

import android.content.Context;

/**
 * Created by Kuo on 2016/1/5.
 */
public interface FindTypeOfMoneyDetailPreseneter {
    void onFind(Context context, String moneyType, String typeName, String startDate, String endDate, int mDayCount);
}
