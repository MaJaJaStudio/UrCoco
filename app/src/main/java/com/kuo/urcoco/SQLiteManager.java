package com.kuo.urcoco;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.kuo.urcoco.common.item.AccountItem;
import com.kuo.urcoco.presenter.account.FindAccountPresenter;
import com.kuo.urcoco.presenter.account.FindAccountPresenterImpl;
import com.kuo.urcoco.view.account.FindAccountView;

import java.util.ArrayList;

/*
 * Created by User on 2015/11/1.
 */
public class SQLiteManager extends SQLiteOpenHelper {

    /*
    * SQLite Version Data;
    * */
    private final static int DB_VERSION = 3;
    private final static String DB_NAME = "MoneyCatSQLite.db";

    /*
    * Public table column name;
    * */
    private final static String ROW_ID = "rowId";
    private final static String ACCOUNT_NAME = "accountName";
    private final static String DATE = "date";

    /*
    * Account table column name;
    * */
    private final static String ACCOUNT_TABLE = "accountTable";

    private final static String MONEY_TABLE_NAME = "moneyTableName";
    private final static String ICON_COLOR = "iconColor";
    private final static String ICON_COLOR_DARK = "iconColorDark";
    private final static String BUDGET = "budget";

    /*
    * Type table column name;
    * */
    private final static String TYPE_TABLE = "typeTable";

    private final static String TYPE_KIND = "typeKind";
    private final static String TYPE_NAME = "typeName";
    private final static String TYPE_PATH = "typePath";
    private final static String TYPE_COLOR = "typeColor";
    private final static String TYPE_COLOR_DARK = "typeColorDark";

    /*
    * Money table column name;
    * */
    private final static String MONEY = "money";
    private final static String CONTENT = "content";
    private final static String MONEY_TYPE = "moneyType";
    private final static String IMAGE = "image";

    private SimpleTransactionListener simpleTransactionListener;

    public void setSimpleTransactionListener(SimpleTransactionListener simpleTransactionListener) {
        this.simpleTransactionListener = simpleTransactionListener;
    }

    Context context;

    public interface SimpleTransactionListener {
        void endTransaction();
    }

    public SQLiteManager (Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createAccountTable = "CREATE TABLE " + ACCOUNT_TABLE
                + " (" + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ACCOUNT_NAME + " TEXT, "
                + MONEY_TABLE_NAME + " TEXT, "
                + ICON_COLOR + " INTEGER, "
                + BUDGET + " INTEGER, "
                + DATE + " TEXT, "
                + ICON_COLOR_DARK + " INTEGER);";

        String createTypeTable = "CREATE TABLE " + TYPE_TABLE
                + " (" + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TYPE_KIND + " TEXT, "
                + TYPE_NAME + " TEXT, "
                + TYPE_PATH + " TEXT, "
                + TYPE_COLOR + " INTEGER, "
                + TYPE_COLOR_DARK + " INTEGER);";

        sqLiteDatabase.execSQL(createAccountTable);
        sqLiteDatabase.execSQL(createTypeTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        if(i2 > i) {

            sqLiteDatabase.beginTransaction();
            boolean success = false;

            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("更新資料庫中...");
            progressDialog.show();

            switch (i) {
                case 1:
                    sqLiteDatabase.execSQL("ALTER TABLE " + ACCOUNT_TABLE + " ADD COLUMN " + ICON_COLOR_DARK + " integer DEFAULT " + ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    i++;
                    success = true;
                    break;
                case 2:

                    Cursor cursor = sqLiteDatabase.query(ACCOUNT_TABLE, new String[]{ROW_ID, ACCOUNT_NAME, MONEY_TABLE_NAME, ICON_COLOR, BUDGET, DATE, ICON_COLOR_DARK}, null, null, null, null, null);


                    if(cursor != null) {
                        cursor.moveToFirst();
                        for(int j = 0 ; j < cursor.getCount() ; j++) {
                            sqLiteDatabase.execSQL("ALTER TABLE " + cursor.getString(2) + " ADD COLUMN " + IMAGE + " BLOD DEFAULT " + null);
                        }
                    }
                    success = true;
                    i++;
                    break;
            }

            if (success) {
                sqLiteDatabase.setTransactionSuccessful();//正確交易才成功
                progressDialog.dismiss();
            }

            sqLiteDatabase.endTransaction();

            if(simpleTransactionListener != null) {
                simpleTransactionListener.endTransaction();
            }

        } else {
            onCreate(sqLiteDatabase);
        }

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    public long insertAccountData(String accountName, String moneyTableName, int color, int budget,String date, int colorDark){

        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNT_NAME, accountName);
        contentValues.put(MONEY_TABLE_NAME, moneyTableName);
        contentValues.put(ICON_COLOR, color);
        contentValues.put(ICON_COLOR_DARK, colorDark);
        contentValues.put(BUDGET, budget);
        contentValues.put(DATE, date);

        return getWritableDatabase().insert(ACCOUNT_TABLE, null, contentValues);
    }

    public long insertMoney(String tableName, String typeName, String moneyType, Integer money, String content, byte[] image, String date) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPE_NAME, typeName);
        contentValues.put(MONEY_TYPE, moneyType);
        contentValues.put(MONEY, money);
        contentValues.put(CONTENT, content);
        contentValues.put(IMAGE, image);
        contentValues.put(DATE, date);

        return getWritableDatabase().insert(tableName, null, contentValues);
    }

    public long insertMoneyData(String tableName, String typeName, String moneyType, Integer money, String content, String date){

        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPE_NAME, typeName);
        contentValues.put(MONEY_TYPE, moneyType);
        contentValues.put(MONEY, money);
        contentValues.put(CONTENT, content);
        contentValues.put(DATE, date);

        return getWritableDatabase().insert(tableName, null, contentValues);
    }

    public long insterTypeData(String typeKind, String typeName, String typePath, int typeColor, int typeColorDark) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPE_KIND, typeKind);
        contentValues.put(TYPE_NAME, typeName);
        contentValues.put(TYPE_PATH, typePath);
        contentValues.put(TYPE_COLOR, typeColor);
        contentValues.put(TYPE_COLOR_DARK, typeColorDark);

        return getWritableDatabase().insert(TYPE_TABLE, null, contentValues);
    }

    public Cursor getAccountData() {
        return getWritableDatabase().query(ACCOUNT_TABLE, new String[]{ROW_ID, ACCOUNT_NAME, MONEY_TABLE_NAME, ICON_COLOR, BUDGET, DATE, ICON_COLOR_DARK}, null, null, null, null, null);
    }

    public Cursor getAccountWhereAccountName(String accountName) {

        Cursor cursor = getWritableDatabase().query(ACCOUNT_TABLE, new String[]{ROW_ID, ACCOUNT_NAME, MONEY_TABLE_NAME, ICON_COLOR, BUDGET, DATE, ICON_COLOR_DARK}, ACCOUNT_NAME + "=" + "'" + accountName + "'", null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public void updateAccountData(int rowId, String accountName, int budget, int color, int colorDark) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNT_NAME, accountName);
        contentValues.put(BUDGET, color);
        contentValues.put(ICON_COLOR, budget);
        contentValues.put(ICON_COLOR_DARK, colorDark);
        getWritableDatabase().update(ACCOUNT_TABLE, contentValues, ROW_ID + "=" + rowId, null);
    }

    public Cursor getMoneyDataDescDateNotRepeat(String tableName){

        Cursor cursor = getWritableDatabase().query(true, tableName, new String[]{DATE}, null, null, null, null, "date(" + DATE + ")" + " DESC", null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor gettMoneyData(String tableName) {
        return getWritableDatabase().query(tableName, new String[]{ROW_ID, TYPE_NAME, MONEY_TYPE, MONEY, CONTENT, DATE}, null, null, null, null, null);
    }

    public Cursor getMoneyDataWhereRowId(String tableName, int id){

        Cursor cursor = getWritableDatabase().query(tableName, new String[]{ROW_ID, TYPE_NAME, MONEY_TYPE, MONEY, CONTENT, DATE}, ROW_ID + "=" + "'" + id + "'", null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor getMoneyDataWhereType(String tableName, String typeName){

        Cursor cursor = getWritableDatabase().query(tableName, new String[]{ROW_ID, TYPE_NAME, MONEY_TYPE, MONEY, CONTENT, DATE}, TYPE_NAME + "=" + "'" + typeName + "'", null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor getMoneyDataWhereTypeAndRangeDate(String tableName, String moneyType, String typeName, String startDate, String endDate){

        Cursor cursor = getWritableDatabase().query(tableName, new String[]{ROW_ID, TYPE_NAME, MONEY_TYPE, MONEY, CONTENT, DATE}, MONEY_TYPE + "=?" + " AND " + TYPE_NAME + "=?" + " AND " + DATE + " BETWEEN ? AND ?", new String[]{moneyType, typeName, startDate, endDate}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor getMoneyDataWhereMoneyType(String tableName, String moneyType){

        Cursor cursor = getWritableDatabase().query(tableName, new String[]{ROW_ID, TYPE_NAME, MONEY_TYPE, MONEY, CONTENT, DATE}, MONEY_TYPE + "=" + "'" + moneyType + "'", null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor getMoneyDataWhereMoneyTypeAndTypeName(String tableName, String typeName, String moneyType){

        Cursor cursor = getWritableDatabase().query(tableName, new String[]{ROW_ID, TYPE_NAME, MONEY_TYPE, MONEY, CONTENT, DATE}, TYPE_NAME + "=" + "'" + typeName + "'" + " AND " + MONEY_TYPE + "=" + "'" + moneyType + "'", null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor getMoneyDataOfDate(String tableName, String date){

        Cursor cursor = getWritableDatabase().query(tableName, new String[]{ROW_ID, TYPE_NAME, MONEY_TYPE, MONEY, CONTENT, DATE}, DATE + "=?", new String[]{date}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor getMoneyDataRangeOfDate(String tableName, String startDate, String endDate) {
        Cursor cursor = getWritableDatabase().query(tableName, new String[]{ROW_ID, TYPE_NAME, MONEY_TYPE, MONEY, CONTENT, DATE, IMAGE}, DATE + " BETWEEN ? AND ?", new String[]{startDate, endDate}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor getMoneyDataRangeOfDateAndType(String tableName, String startDate, String endDate, String moneyType) {
        Cursor cursor = getWritableDatabase().query(tableName, new String[]{ROW_ID, TYPE_NAME, MONEY_TYPE, MONEY, CONTENT, DATE}, DATE + " BETWEEN ? AND ?" + " AND " + MONEY_TYPE + "= ?", new String[]{startDate, endDate, moneyType}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public boolean getSresultByName(String name) {

        Cursor cursor = getWritableDatabase().query(ACCOUNT_TABLE, new String[]{ROW_ID, ACCOUNT_NAME}, ACCOUNT_NAME + "=" + "'" + name + "'", null, null, null, null);

        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }

    }

    public Cursor getTypeDataWhereTypeName(String typeName) {

        Cursor cursor = getWritableDatabase().query(TYPE_TABLE, new String[]{ROW_ID, TYPE_PATH, TYPE_COLOR, TYPE_COLOR_DARK}, TYPE_NAME + "=" + "'" + typeName + "'", null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;

    }

    public Cursor getTypeData(String typeKind) {

        Cursor cursor = getWritableDatabase().query(TYPE_TABLE, new String[]{TYPE_KIND, TYPE_NAME, TYPE_PATH, TYPE_COLOR, TYPE_COLOR_DARK}, TYPE_KIND + "=" + "'" + typeKind + "'", null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;

    }

    public long updateMoney(String tableName, int rowId, String typeName, String moneyType, Integer money, String content, String date, byte[] image) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPE_NAME, typeName);
        contentValues.put(MONEY_TYPE, moneyType);
        contentValues.put(MONEY, money);
        contentValues.put(CONTENT, content);
        contentValues.put(IMAGE, image);
        contentValues.put(DATE, date);

        return getWritableDatabase().update(tableName, contentValues, ROW_ID + "=" + rowId, null);
    }

    public void updateMoneyData(String tableName, int rowId, String typeName, String moneyType, Integer money, String content, String date) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPE_NAME, typeName);
        contentValues.put(MONEY_TYPE, moneyType);
        contentValues.put(MONEY, money);
        contentValues.put(CONTENT, content);

        getWritableDatabase().update(tableName, contentValues, ROW_ID + "=" + rowId, null);
    }

    public void deleteMoneyData(String tableName, int rowId) {
        getWritableDatabase().delete(tableName, ROW_ID + "=" + rowId, null);
    }

    public void deleteTable(String tableName) {
        getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public void deleteAccount(int rowId) {
        getWritableDatabase().delete(ACCOUNT_TABLE, ROW_ID + "=" + rowId, null);
    }

    public void onCreateMoneyTable(String tableName) {

        String createDefaultTable = "CREATE TABLE " + tableName
                + " (" + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TYPE_NAME + " TEXT, "
                + MONEY_TYPE + " TEXT, "
                + MONEY + " INTEGER, "
                + CONTENT + " TEXT, "
                + IMAGE + " BLOD, "
                + DATE + " TEXT);";

        getWritableDatabase().execSQL(createDefaultTable);
    }
}
