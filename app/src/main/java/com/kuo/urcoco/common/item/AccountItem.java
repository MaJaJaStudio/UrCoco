package com.kuo.urcoco.common.item;

import java.io.Serializable;

/*
 * Created by User on 2015/11/8.
 */
public class AccountItem implements Serializable {

    private int rowId;
    private int budget;
    private int color;
    private int colorDark;

    private String accountName;
    private String moneyTableName;
    private String iconPath;

    private boolean check;

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setMoneyTableName(String moneyTableName) {
        this.moneyTableName = moneyTableName;
    }

    public String getMoneyTableName() {
        return moneyTableName;
    }

    public void setColorDark(int colorDark) {
        this.colorDark = colorDark;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public int getColorDark() {
        return colorDark;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getRowId() {
        return rowId;
    }

    public int getBudget() {
        return budget;
    }

    public String getAccountName() {
        return accountName;
    }

    public boolean getCheck() {
        return check;
    }

}
