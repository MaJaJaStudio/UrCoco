package com.kuo.urcoco.model.money;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

import com.kuo.urcoco.SQLiteManager;
import com.kuo.urcoco.common.item.CurrentAccountData;
import com.kuo.urcoco.common.item.MoneyItem;
import com.kuo.urcoco.view.OnFoundMoneyItemsListener;

import java.util.ArrayList;

/**
 * Created by Kuo on 2015/12/11.
 */
public class FindMoneyOfDayInteractorImpl implements FindMoneyItemsInteractor {

    @Override
    public void findMoneyOfDay(final OnFoundMoneyItemsListener onFoundMoneyItemsListener, final Context context, final String date) {


        new Handler().post(new Runnable() {
            @Override
            public void run() {

                SQLiteManager sqLiteManager = new SQLiteManager(context);
                sqLiteManager.onOpen(sqLiteManager.getWritableDatabase());
                Cursor cursor = sqLiteManager.getMoneyDataOfDate(CurrentAccountData.getMoneyTableName(), date);
                ArrayList<MoneyItem> moneyItems = new ArrayList<>();
                int totalMoney = 0;

                if (cursor != null) {
                    cursor.moveToFirst();
                    for (int j = 0; j < cursor.getCount(); j++) {

                        Cursor cursorType = sqLiteManager.getTypeDataWhereTypeName(cursor.getString(1));
                        MoneyItem moneyItem = new MoneyItem();
                        moneyItem.setDate(date);
                        moneyItem.setRowId(cursor.getInt(0));
                        moneyItem.setTitleText(cursor.getString(1));
                        moneyItem.setColor(cursorType.getInt(2));
                        moneyItem.setCost(cursor.getInt(3));
                        moneyItem.setContentText(cursor.getString(4));
                        moneyItem.setMONEY_TYPE(cursor.getString(2));
                        moneyItems.add(moneyItem);

                        Log.d("Money", cursor.getInt(3) + "");

                        if (cursor.getString(2).equals("expense"))
                            totalMoney += (cursor.getInt(3) * -1);
                        else
                            totalMoney += cursor.getInt(3);

                        cursor.moveToNext();
                    }
                }
                onFoundMoneyItemsListener.onFoundMoneyItems(moneyItems, totalMoney);
            }
        });
    }

}
