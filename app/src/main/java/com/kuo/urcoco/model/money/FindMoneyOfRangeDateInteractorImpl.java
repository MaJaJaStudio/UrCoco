package com.kuo.urcoco.model.money;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;

import com.kuo.urcoco.SQLiteManager;
import com.kuo.urcoco.common.item.CurrentAccountData;
import com.kuo.urcoco.common.item.MoneyItem;
import com.kuo.urcoco.view.OnFoundMoneyItemsListener;

import java.util.ArrayList;

/**
 * Created by User on 2015/12/27.
 */
public class FindMoneyOfRangeDateInteractorImpl implements FindMoneyOfRangeDateInteractor {

    @Override
    public void findMoney(final OnFoundMoneyItemsListener onFoundMoneyItemsListener, final Context context, final String startDate, final String endDate) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                SQLiteManager sqLiteManager = new SQLiteManager(context);
                sqLiteManager.onOpen(sqLiteManager.getReadableDatabase());

                Cursor cursor = sqLiteManager.getMoneyDataRangeOfDate(CurrentAccountData.getMoneyTableName(), startDate, endDate);
                ArrayList<MoneyItem> moneyItems = new ArrayList<>();
                int totalMoney = 0;

                if (cursor != null) {
                    cursor.moveToFirst();
                    for (int j = 0; j < cursor.getCount(); j++) {

                        Cursor cursorType = sqLiteManager.getTypeDataWhereTypeName(cursor.getString(1));
                        MoneyItem moneyItem = new MoneyItem();

                        moneyItem.setColor(cursorType.getInt(2));
                        moneyItem.setColorPrimaryDark(cursorType.getInt(3));
                        moneyItem.setRowId(cursor.getInt(0));
                        moneyItem.setTitleText(cursor.getString(1));
                        moneyItem.setMONEY_TYPE(cursor.getString(2));
                        moneyItem.setCost(cursor.getInt(3));
                        moneyItem.setContentText(cursor.getString(4));
                        moneyItem.setDate(cursor.getString(5));
                        moneyItems.add(moneyItem);

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
