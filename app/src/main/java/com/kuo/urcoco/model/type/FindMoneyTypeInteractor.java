package com.kuo.urcoco.model.type;

import android.content.Context;

import com.kuo.urcoco.view.OnFoundMoneyTypeListener;

/**
 * Created by Kuo on 2015/12/10.
 */
public interface FindMoneyTypeInteractor {
    void findMoneyType(OnFoundMoneyTypeListener onFoundMoneyTypeListener, Context context, String kind);
}
