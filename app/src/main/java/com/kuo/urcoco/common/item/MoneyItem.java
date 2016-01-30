package com.kuo.urcoco.common.item;

import java.io.Serializable;

/**
 * Created by Kuo on 2015/10/23.
 */
public class MoneyItem implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final int MONEY_TYPE_COST = 201501;
    public static final int MONEY_TYPE_INCOME = 201502;

    private int rowId, iconImage, cost, year, month, day, color, mColorPrimaryDark;
    private int iconPath;
    private String titleText;
    private String contentText;
    private String tableName;
    private String date;
    private String MONEY_TYPE;
    private byte[] image;
    private boolean isCheck = false;

    private int layoutPosition;

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public void setLayoutPosition(int layoutPosition) {
        this.layoutPosition = layoutPosition;
    }

    public int getLayoutPosition() {
        return layoutPosition;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTableName() {
        return tableName;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public boolean getCheck() {
        return isCheck;
    }

    public void setMONEY_TYPE(String MONEY_TYPE) {
        this.MONEY_TYPE = MONEY_TYPE;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setIconImage(int iconImage) {
        this.iconImage = iconImage;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getTitleText() {
        return titleText;
    }

    public int getIconImage() {
        return iconImage;
    }

    public int getCost() {
        return cost;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getRowId() {
        return rowId;
    }

    public String getContentText() {
        return contentText;
    }

    public String getMONEY_TYPE() {
        return MONEY_TYPE;
    }

    public String getDate() {
        return date;
    }

    public int getColor() {
        return color;
    }

    public void setColorPrimaryDark(int colorPrimaryDark) {
        this.mColorPrimaryDark = colorPrimaryDark;
    }

    public int getColorPrimaryDark() {
        return mColorPrimaryDark;
    }
}
