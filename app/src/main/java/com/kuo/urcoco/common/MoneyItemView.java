package com.kuo.urcoco.common;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kuo.urcoco.R;

/**
 * Created by Kuo on 2015/11/12.
 */
public class MoneyItemView extends RelativeLayout {

    private int typePath = 0;
    private int money = 0;
    private int itemCount = 0;
    private int color;
    private String typeName = "";

    public MoneyItemView(Context context) {
        super(context);
    }

    public MoneyItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onCreateView() {

        LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.money_detail_item, this);

        setClickable(true);
        initIconImage();
        initTextView();
    }

    private void initInfoLayout() {

        RelativeLayout infoLayout = (RelativeLayout) findViewById(R.id.infoLayout);

    }

    private void initIconImage() {

        CircleTextView iconImage = (CircleTextView) findViewById(R.id.iconImage);
        iconImage.setCircleColor(color);
        iconImage.setTextColor(Color.parseColor("#FFFFFF"));
        iconImage.setText(String.valueOf(typeName.charAt(0)));

    }

    private void initTextView() {

        TextView titleText = (TextView) findViewById(R.id.titleText);
        TextView costText = (TextView) findViewById(R.id.costText);
        TextView itemsText = (TextView) findViewById(R.id.itemsText);

        titleText.setText(typeName);
        costText.setText("$ " + money);
        itemsText.setText(itemCount + "項");

    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setTypePath(int typePath) {
        this.typePath = typePath;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public void setCircleColor(int color) {
        this.color = color;
    }

}
