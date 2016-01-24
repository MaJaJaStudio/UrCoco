package com.kuo.urcoco.presenter.account;

import android.content.Context;

import com.kuo.urcoco.common.item.AccountItem;
import com.kuo.urcoco.view.account.FindAccountView;
import com.kuo.urcoco.view.OnFindAcountListener;
import com.kuo.urcoco.model.account.FindAccountInteractor;
import com.kuo.urcoco.model.account.FindAccountInteractorImpl;

import java.util.ArrayList;

/**
 * Created by Kuo on 2015/12/14.
 */
public class FindAccountPresenterImpl implements FindAccountPresenter, OnFindAcountListener {

    private FindAccountView findAccountView;
    private FindAccountInteractor findAccountInteractor;

    public FindAccountPresenterImpl(FindAccountView findAccountView) {
        this.findAccountView = findAccountView;
        findAccountInteractor = new FindAccountInteractorImpl();
    }

    @Override
    public void findAccount(Context context) {
        findAccountInteractor.findAccount(this, context);
    }

    @Override
    public void onFinished(ArrayList<AccountItem> accountItems) {
        findAccountView.onFoundAccount(accountItems);
    }
}
