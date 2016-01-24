package com.kuo.urcoco.model.sql;

import android.content.Context;

import com.kuo.urcoco.view.OnCreateSQLiteListener;

/**
 * Created by Kuo on 2015/12/14.
 */
public interface CreateSQLiteInteractor {
    void createSQLite(OnCreateSQLiteListener onCreateSQLiteListener, Context context);
}
