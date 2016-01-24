package com.kuo.urcoco.model.money;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;

import com.kuo.urcoco.SQLiteManager;
import com.kuo.urcoco.common.item.CurrentAccountData;
import com.kuo.urcoco.view.OnFoundIntegerListener;

import java.util.Calendar;

/*
 * Created by Kuo on 2015/12/11.
 */
public class FindCostOfMonthInteractorImpl implements FindCostOfMonthInteractor {

    @Override
    public void findInteger(final OnFoundIntegerListener onFoundIntegerListener, final Context context, final int year, final int month) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();

                SQLiteManager sqLiteManager = new SQLiteManager(context);
                sqLiteManager.onOpen(sqLiteManager.getWritableDatabase());
                int totalMoney = 0;


                String formatStr = "%02d";
                String mMonth = String.format(formatStr, month);
                String day = String.format(formatStr, getLastDayOfMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)));

                String startDate = year + "-" + mMonth + "-" + "01";
                String endDate = year + "-" + mMonth + "-" + day;

                Cursor cursor = sqLiteManager.getMoneyDataRangeOfDate(CurrentAccountData.getMoneyTableName(), startDate, endDate);

                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        if (cursor.getString(2).equals("expense"))
                            totalMoney += cursor.getInt(3);

                        cursor.moveToNext();
                    }
                }
                sqLiteManager.close();
                onFoundIntegerListener.OnFoundInteger(totalMoney);
            }
        });
    }

    private int getLastDayOfMonth(int year, int month) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
