package com.kuo.urcoco.common;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuo.urcoco.R;

/**
 * Created by User on 2015/11/22.
 */
public class HeaderAccountView extends LinearLayout {
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

    public void setAccountText(String text) {

        TextView textView = (TextView) findViewById(R.id.nav_account_text);
        textView.setText(text);

    }

    public String getAccountText() {

        TextView textView = (TextView) findViewById(R.id.nav_account_text);

        return textView.getText().toString();
    }

    public void setCircleTextView(String text, int color) {

        CircleTextView circleTextView = (CircleTextView) findViewById(R.id.nav_account_image);
        circleTextView.setText(text);
        circleTextView.setTextColor(Color.parseColor("#FFFFFF"));
        circleTextView.setCircleColor(color);
    }
}
