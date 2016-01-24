package com.kuo.urcoco.view.money;

/**
 * Created by Kuo on 2016/1/5.
 */
public interface FoundDetailMoneyView {
    void onFoundTotalMoney(int totalMoney);
    void onFoundMuchMoney(int muchMoney);
    void onFoundAvgMoneyOfDay(double avgMoney);
    void onFoundTotalQuantity(int totalQuantity);
    void onFoundUsedToDayOfWeek(String usedToDayOfWeek);
}
