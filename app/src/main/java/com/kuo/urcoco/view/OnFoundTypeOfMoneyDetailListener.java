package com.kuo.urcoco.view;

/**
 * Created by Kuo on 2016/1/5.
 */
public interface OnFoundTypeOfMoneyDetailListener {
    void onFoundTotalMoney(int totalMoney);
    void onFoundMuchMoney(int muchMoney);
    void onFoundAvgMoneyOfDay(double avgMoney);
    void onFoundTotalQuantity(int totalQuantity);
    void onFoundUsedToDayOfWeek(String usedToDayOfWeek);
}
