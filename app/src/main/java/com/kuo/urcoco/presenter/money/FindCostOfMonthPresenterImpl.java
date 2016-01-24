package com.kuo.urcoco.presenter.money;

import android.content.Context;

import com.kuo.urcoco.model.money.FindCostOfMonthInteractor;
import com.kuo.urcoco.model.money.FindCostOfMonthInteractorImpl;
import com.kuo.urcoco.view.OnFoundIntegerListener;
import com.kuo.urcoco.view.money.FindCostOfMonthView;

/**
 * Created by Kuo on 2015/12/11.
 */
public class FindCostOfMonthPresenterImpl implements FindCostOfMonthPresenter, OnFoundIntegerListener {

    private FindCostOfMonthView findCostOfMonthView;
    private FindCostOfMonthInteractor findCostOfMonthInteractor;

    public FindCostOfMonthPresenterImpl(FindCostOfMonthView findCostOfMonthView) {
        this.findCostOfMonthView = findCostOfMonthView;
        findCostOfMonthInteractor = new FindCostOfMonthInteractorImpl();
    }

    @Override
    public void onFindCostOfMonth(Context context, int year, int month) {
        findCostOfMonthInteractor.findInteger(this, context, year, month);
    }

    @Override
    public void OnFoundInteger(int i) {
        findCostOfMonthView.onFoundCostOfMonth(i);
    }
}
