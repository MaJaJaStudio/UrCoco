package com.kuo.urcoco.common.item;

/**
 * Created by Kuo on 2015/12/16.
 */
public class SettingItem {

    private String text;
    private int iconResId;

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
