package com.kuo.urcoco.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kuo.urcoco.R;
import com.kuo.urcoco.common.ColorCircle;

import java.util.ArrayList;

/**
 * Created by User on 2015/11/30.
 */
public class ColorCircleAdapter extends BaseAdapter {

    private ArrayList<Integer> colors = new ArrayList<>();
    private Context context;

    private int focus = -1;
    private int focusId = -1;

    public ColorCircleAdapter(Context context, ArrayList<Integer> colors) {
        this.colors = colors;
        this.context = context;
    }

    @Override
    public int getCount() {
        return colors.size();
    }

    @Override
    public Object getItem(int position) {
        return colors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_colorcircle_item, null);

        ColorCircle colorCircle = (ColorCircle) view.findViewById(R.id.colorCircle);
        colorCircle.setColor(colors.get(position));

        return view;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    public void setFocusId(int focusId) {
        this.focusId = focusId;
    }

    public int getFocus() {
        return focus;
    }

    public int getFocusId() {
        return focusId;
    }

    public int getColor(int index) {
        return this.colors.get(index);
    }

}
