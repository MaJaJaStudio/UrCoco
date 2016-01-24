package com.kuo.urcoco.model.account;

import android.content.Context;
import android.database.Cursor;

import com.kuo.urcoco.SQLiteManager;
import com.kuo.urcoco.common.item.AccountItem;
import com.kuo.urcoco.view.OnFindAcountListener;

import java.util.ArrayList;

/*
 * Created by Kuo on 2015/11/19.
 */
public class FindAccountInteractorImpl implements FindAccountInteractor {

    @Override
    public void findAccount(OnFindAcountListener onFindAcountListener, Context context) {
        onFindAcountListener.onFinished(getFirstOneAccountItem(context));
    }

    public ArrayList<AccountItem> getFirstOneAccountItem(Context context) {

        SQLiteManager sqLiteManager = new SQLiteManager(context);
        sqLiteManager.onOpen(sqLiteManager.getWritableDatabase());

        Cursor cursor = sqLiteManager.getAccountData();
        ArrayList<AccountItem> accountItems = new ArrayList<>();

        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            for(int i = 0 ; i < cursor.getCount() ; i++) {
                AccountItem accountItem = new AccountItem();
                accountItem.setRowId(cursor.getInt(0));
                accountItem.setAccountName(cursor.getString(1));
                accountItem.setMoneyTableName(cursor.getString(2));
                accountItem.setColor(cursor.getInt(3));
                accountItem.setColorDark(cursor.getInt(6));
                accountItem.setBudget(cursor.getInt(4));
                accountItems.add(accountItem);
                cursor.moveToNext();
            }
        }

        sqLiteManager.close();

        return accountItems;
    }
}
