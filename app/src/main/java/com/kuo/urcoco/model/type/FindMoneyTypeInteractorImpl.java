package com.kuo.urcoco.model.type;

import android.content.Context;
import android.database.Cursor;

import com.kuo.urcoco.SQLiteManager;
import com.kuo.urcoco.common.item.TypeItem;
import com.kuo.urcoco.view.OnFoundMoneyTypeListener;

import java.util.ArrayList;

/**
 * Created by Kuo on 2015/12/10.
 */
public class FindMoneyTypeInteractorImpl implements FindMoneyTypeInteractor {

    @Override
    public void findMoneyType(OnFoundMoneyTypeListener onFoundMoneyTypeListener, Context context, String kind) {
        onFoundMoneyTypeListener.onFoundMoneyType(getMoneyType(context, kind));
    }

    private ArrayList<TypeItem> getMoneyType(Context context, String kind) {

        SQLiteManager sqLiteManager = new SQLiteManager(context);
        sqLiteManager.onOpen(sqLiteManager.getWritableDatabase());

        Cursor cursor = sqLiteManager.getTypeData(kind);
        ArrayList<TypeItem> typeItems = new ArrayList<>();

        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            for(int i = 0 ; i < cursor.getCount() ; i++) {
                TypeItem typeItem = new TypeItem();
                typeItem.setTypeKind(cursor.getString(0));
                typeItem.setTypeName(cursor.getString(1));
                typeItem.setTypePath(cursor.getString(2));
                typeItem.setTypeColor(cursor.getInt(3));
                typeItem.setTypeColorDark(cursor.getInt(4));
                typeItems.add(typeItem);
                cursor.moveToNext();
            }
        }
        return typeItems;
    }
}
