package com.kuo.urcoco.common;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuo.urcoco.R;
import com.kuo.urcoco.common.item.AccountItem;

/**
 * Created by User on 2015/11/22.
 */
public class HeaderAccountView extends LinearLayout {

    private AccountItem mAccountItem;

    public HeaderAccountView(Context context) {
        super(context);

        onCreateView();
    }

    public HeaderAccountView(Context context, AttributeSet attrs) {
        super(context, attrs);

        onCreateView();
    }

    private void onCreateView() {

        LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.nav_header_account, this);

    }

    public String getAccountName() {
        return mAccountItem.getAccountName();
    }


    public void setAccount(AccountItem accountItem) {

        mAccountItem = accountItem;

        TextView textView = (TextView) findViewById(R.id.nav_account_text);
        textView.setText(accountItem.getAccountName());

        CircleTextView circleTextView = (CircleTextView) findViewById(R.id.nav_account_image);
        circleTextView.setText(String.valueOf(accountItem.getAccountName().charAt(0)));
        circleTextView.setTextColor(Color.parseColor("#FFFFFF"));
        circleTextView.setCircleColor(accountItem.getColor());
    }

    public AccountItem getAccountItem() {
        return mAccountItem;
    }
}
