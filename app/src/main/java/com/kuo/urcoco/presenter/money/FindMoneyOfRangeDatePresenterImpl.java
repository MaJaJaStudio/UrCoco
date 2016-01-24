package com.kuo.urcoco.presenter.money;

import android.content.Context;

import com.kuo.urcoco.common.item.MoneyItem;
import com.kuo.urcoco.model.money.FindMoneyOfRangeDateInteractor;
import com.kuo.urcoco.model.money.FindMoneyOfRangeDateInteractorImpl;
import com.kuo.urcoco.view.OnFoundMoneyItemsListener;
import com.kuo.urcoco.view.money.FindMoneyItemsView;

import java.util.ArrayList;

/**
 * Created by User on 2015/12/27.
 */
public class FindMoneyOfRangeDatePresenterImpl implements OnFoundMoneyItemsListener, FindMoneyOfRangeDatePresenter{

    private FindMoneyItemsView findMoneyItemsView;
    private FindMoneyOfRangeDateInteractor findMoneyOfRangeDateInteractor;

    public FindMoneyOfRangeDatePresenterImpl(FindMoneyItemsView findMoneyItemsView) {
        this.findMoneyItemsView = findMoneyItemsView;
        findMoneyOfRangeDateInteractor = new FindMoneyOfRangeDateInteractorImpl();
    }

    @Override
    public void onFoundMoneyItems(ArrayList<MoneyItem> moneyItems, int totalMoney) {
        findMoneyItemsView.onFoundMoneyItems(moneyItems, totalMoney);
    }

    @Override
    public void onFindMoneyItems(Context context, String startDate, String endDate) {
        findMoneyOfRangeDateInteractor.findMoney(this, context, startDate, endDate);
    }
}
