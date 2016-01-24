package com.kuo.urcoco.presenter.money;

import android.content.Context;

import com.kuo.urcoco.model.money.FindTypeOfMoneyDetailInteractor;
import com.kuo.urcoco.model.money.FindTypeOfMoneyDetailInteractorImpl;
import com.kuo.urcoco.view.OnFoundTypeOfMoneyDetailListener;
import com.kuo.urcoco.view.money.FoundDetailMoneyView;

/**
 * Created by Kuo on 2016/1/5.
 */
public class FindTypeOfMoneyDetailPreseneterImpl implements OnFoundTypeOfMoneyDetailListener, FindTypeOfMoneyDetailPreseneter {

    FoundDetailMoneyView foundDetailMoneyView;
    FindTypeOfMoneyDetailInteractor findTypeOfMoneyDetailInteractor;

    public FindTypeOfMoneyDetailPreseneterImpl(FoundDetailMoneyView foundDetailMoneyView) {
        this.foundDetailMoneyView = foundDetailMoneyView;
        findTypeOfMoneyDetailInteractor = new FindTypeOfMoneyDetailInteractorImpl();
    }

    @Override
    public void onFoundTotalMoney(int totalMoney) {
        foundDetailMoneyView.onFoundTotalMoney(totalMoney);
    }

    @Override
    public void onFoundMuchMoney(int muchMoney) {
        foundDetailMoneyView.onFoundMuchMoney(muchMoney);
    }

    @Override
    public void onFoundAvgMoneyOfDay(double avgMoney) {
        foundDetailMoneyView.onFoundAvgMoneyOfDay(avgMoney);
    }

    @Override
    public void onFoundTotalQuantity(int totalQuantity) {
        foundDetailMoneyView.onFoundTotalQuantity(totalQuantity);
    }

    @Override
    public void onFoundUsedToDayOfWeek(String usedToDayOfWeek) {
        foundDetailMoneyView.onFoundUsedToDayOfWeek(usedToDayOfWeek);
    }

    @Override
    public void  onFind(Context context, String moneyType, String typeName, String startDate, String endDate, int mDayCount) {
        findTypeOfMoneyDetailInteractor.onFind(this, context, moneyType, typeName, startDate, endDate, mDayCount);
    }
}
