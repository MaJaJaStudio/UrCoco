package com.kuo.urcoco.presenter.money;

import android.content.Context;

import com.kuo.urcoco.common.item.MoneyItem;
import com.kuo.urcoco.model.money.FindMoneyItemsInteractor;
import com.kuo.urcoco.model.money.FindMoneyOfDayInteractorImpl;
import com.kuo.urcoco.view.OnFoundMoneyItemsListener;
import com.kuo.urcoco.view.money.FindMoneyItemsView;

import java.util.ArrayList;

/*
 * Created by Kuo on 2015/12/11.
 */
public class FindMoneyOfDayPresenterImpl implements FindMoneyItemsPresenter, OnFoundMoneyItemsListener {

    private FindMoneyItemsView findMoneyItemsView;
    private FindMoneyItemsInteractor findMoneyItemsInteractor;

    public FindMoneyOfDayPresenterImpl(FindMoneyItemsView findMoneyItemsView) {
        this.findMoneyItemsView = findMoneyItemsView;
        findMoneyItemsInteractor = new FindMoneyOfDayInteractorImpl();
    }

    @Override
    public void onFindMoneyItems(Context context, String date) {
        findMoneyItemsInteractor.findMoneyOfDay(this, context, date);
    }

    @Override
    public void onFoundMoneyItems(ArrayList<MoneyItem> moneyItems, int totalMoney) {
        findMoneyItemsView.onFoundMoneyItems(moneyItems, totalMoney);
    }
}
