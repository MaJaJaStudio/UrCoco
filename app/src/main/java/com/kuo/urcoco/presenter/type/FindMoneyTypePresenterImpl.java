package com.kuo.urcoco.presenter.type;

import android.content.Context;

import com.kuo.urcoco.common.item.TypeItem;
import com.kuo.urcoco.model.type.FindMoneyTypeInteractor;
import com.kuo.urcoco.model.type.FindMoneyTypeInteractorImpl;
import com.kuo.urcoco.view.OnFoundMoneyTypeListener;
import com.kuo.urcoco.view.type.FindMoneyTypeView;

import java.util.ArrayList;

/**
 * Created by Kuo on 2015/12/10.
 */
public class FindMoneyTypePresenterImpl implements OnFoundMoneyTypeListener, FindMoneyTypePresenter {

    private FindMoneyTypeView findMoneyTypeView;
    private FindMoneyTypeInteractor findMoneyTypeInteractor;

    public FindMoneyTypePresenterImpl(FindMoneyTypeView findMoneyTypeView) {
        this.findMoneyTypeView = findMoneyTypeView;
        findMoneyTypeInteractor = new FindMoneyTypeInteractorImpl();
    }

    @Override
    public void onFindMoneyType(Context context, String kind) {
        findMoneyTypeInteractor.findMoneyType(this, context, kind);
    }

    @Override
    public void onFoundMoneyType(ArrayList<TypeItem> typeItems) {
        findMoneyTypeView.onFoundMoneyType(typeItems);
    }
}
