package com.kuo.urcoco.presenter.days;

import com.kuo.urcoco.model.days.FindRangeDateInteractor;
import com.kuo.urcoco.model.days.FindRangeDateInteractorImpl;
import com.kuo.urcoco.view.OnFoundDateListener;
import com.kuo.urcoco.view.days.FindDateView;

import java.util.ArrayList;

/**
 * Created by User on 2015/12/26.
 */
public class FindRangeDatePresenterImpl implements FindRangeDatePresenter, OnFoundDateListener {

    private FindDateView findDateView;
    private FindRangeDateInteractor findRangeDateInteractor;

    public FindRangeDatePresenterImpl(FindDateView findDateView) {
        this.findDateView = findDateView;
        findRangeDateInteractor = new FindRangeDateInteractorImpl();
    }

    @Override
    public void findRangeDate(String startDate, String endDate) {
        findRangeDateInteractor.findRangeDate(this, startDate, endDate);
    }

    @Override
    public void onFoundDate(ArrayList<String> date) {
        findDateView.onFoundDate(date);
    }
}
