package com.kuo.urcoco.presenter.days;

import com.kuo.urcoco.model.days.FindDaysInteractor;
import com.kuo.urcoco.model.days.FindDaysInteractorImpl;
import com.kuo.urcoco.view.OnFoundDateListener;
import com.kuo.urcoco.view.days.FindDateView;

import java.util.ArrayList;

/**
 * Created by Kuo on 2015/12/11.
 */
public class FindDaysPresenterImpl implements OnFoundDateListener, FindDaysPresenter {

    private FindDateView findDateView;
    private FindDaysInteractor findDaysInteractor;

    public FindDaysPresenterImpl(FindDateView findDateView) {
        this.findDateView = findDateView;
        findDaysInteractor  = new FindDaysInteractorImpl();
    }

    @Override
    public void onFindDays(int year, int month, int day, int times) {
        findDaysInteractor.findDays(this, year, month, day, times);
    }

    @Override
    public void onFoundDate(ArrayList<String> date) {
        findDateView.onFoundDate(date);
    }
}
