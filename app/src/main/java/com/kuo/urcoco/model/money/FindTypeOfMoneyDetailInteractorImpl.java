package com.kuo.urcoco.model.money;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;

import com.kuo.urcoco.SQLiteManager;
import com.kuo.urcoco.common.item.CurrentAccountData;
import com.kuo.urcoco.view.OnFoundTypeOfMoneyDetailListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 * Created by Kuo on 2016/1/5.
 */
public class FindTypeOfMoneyDetailInteractorImpl implements FindTypeOfMoneyDetailInteractor {

    //int mTotalQuantity, mMuchMoney;
    int mTotalMoney;
    //double mAvgDayOfMoney;
    //String mUsedToDayOfWeek;

    @Override
    public void onFind(final OnFoundTypeOfMoneyDetailListener onFoundTypeOfMoneyDetailListener, final Context context, final String moneyType, final String typeName, final String startDate, final String endDate, final int mDayCount) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                SQLiteManager sqLiteManager = new SQLiteManager(context);
                sqLiteManager.onOpen(sqLiteManager.getReadableDatabase());

                Set<Integer> set = new HashSet<>();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                Cursor cursor = sqLiteManager.getMoneyDataWhereTypeAndRangeDate(CurrentAccountData.getMoneyTableName(), moneyType, typeName, startDate, endDate);

                int[] money = new int[cursor.getCount()];
                int[] dayOfWeek = new int[cursor.getCount()];

                for(int i = 0 ; i < cursor.getCount() ; i++) {

                    mTotalMoney += cursor.getInt(3);
                    money[i] = cursor.getInt(3);

                    try {
                        calendar.setTime(simpleDateFormat.parse(cursor.getString(5)));
                        dayOfWeek[i] = calendar.get(Calendar.DAY_OF_WEEK);
                        set.add(calendar.get(Calendar.DAY_OF_WEEK));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                int[] dayOfWeeks = new int[set.size()];
                int[] dayOfCount = new int[set.size()];
                int count = 0;
                int times = 0;

                Iterator<Integer> it = set.iterator();

                while(it.hasNext()) {
                    int num = it.next();
                    for(int i = 0 ; i < dayOfWeek.length ; i++) {
                        if (dayOfWeek[i] == num)
                            count++;
                    }
                    dayOfWeeks[times] = num;
                    dayOfCount[times] = count;
                    count = 0;
                    times++;
                }

                set.clear();

                for(int i = 0 ; i < dayOfCount.length-1 ; i++) {
                    for (int j = 0 ; j < dayOfCount.length-i-1 ; j++) {
                        if (dayOfCount[j+1] > dayOfCount[j]) {
                            int temp = dayOfCount[j+1];
                            dayOfCount[j+1] = dayOfCount[j];
                            dayOfCount[j]= temp;

                            temp = dayOfWeeks[j+1];
                            dayOfWeeks[j+1] = dayOfWeeks[j];
                            dayOfWeeks[j]= temp;
                        }
                    }
                }

                Arrays.sort(money);

                onFoundTypeOfMoneyDetailListener.onFoundAvgMoneyOfDay(mTotalMoney / mDayCount);
                onFoundTypeOfMoneyDetailListener.onFoundMuchMoney(money[money.length - 1]);
                onFoundTypeOfMoneyDetailListener.onFoundTotalMoney(mTotalMoney);
                onFoundTypeOfMoneyDetailListener.onFoundUsedToDayOfWeek(getChineseDayOfWeek(dayOfWeeks[0]));
                onFoundTypeOfMoneyDetailListener.onFoundTotalQuantity(cursor.getCount());
                //mAvgDayOfMoney = mTotalMoney / mDayCount;
                //mUsedToDayOfWeek = getChineseDayOfWeek(dayOfWeeks[0]);
                //mMuchMoney = money[money.length-1];
                //mTotalQuantity = mRowIds.size();
            }
        });
    }

    private String getChineseDayOfWeek(int position) {

        String[] strings = {"日", "一", "二", "三", "四", "五", "六"};

        return strings[position - 1];
    }
}
