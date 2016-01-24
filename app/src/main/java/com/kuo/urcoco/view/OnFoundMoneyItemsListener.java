package com.kuo.urcoco.view;

import com.kuo.urcoco.common.item.MoneyItem;

import java.util.ArrayList;

/*
 * Created by Kuo on 2015/12/11.
 */
public interface OnFoundMoneyItemsListener {
    void onFoundMoneyItems(ArrayList<MoneyItem> moneyItems, int totalMoney);
}
