package com.kuo.urcoco.presenter.sql;

import android.content.Context;

import com.kuo.urcoco.view.sql.CreateSQLiteView;
import com.kuo.urcoco.view.OnCreateSQLiteListener;
import com.kuo.urcoco.model.sql.CreateSQLiteInteractor;
import com.kuo.urcoco.model.sql.CreateSQLiteInteractorImpl;

/**
 * Created by Kuo on 2015/12/14.
 */
public class CreateSQLitePresenterImpl implements CreateSQLitePresenter, OnCreateSQLiteListener {

    private CreateSQLiteView createSQLiteView;
    private CreateSQLiteInteractor createSQLiteInteractor;

    public CreateSQLitePresenterImpl(CreateSQLiteView createSQLiteView) {
        this.createSQLiteView = createSQLiteView;
        createSQLiteInteractor = new CreateSQLiteInteractorImpl();
    }

    @Override
    public void createSQLite(Context context) {
        createSQLiteInteractor.createSQLite(this, context);
    }

    @Override
    public void message(String msg, boolean i) {
        createSQLiteView.message(msg, i);
    }

}
