package com.kuo.urcoco.model.account;

import android.content.Context;

import com.kuo.urcoco.view.OnFindAcountListener;

/**
 * Created by Kuo on 2015/11/19.
 */
public interface FindAccountInteractor {
    void findAccount(OnFindAcountListener onFindAcountListener, Context context);
}
