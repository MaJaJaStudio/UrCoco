package com.kuo.urcoco.common.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kuo.urcoco.R;
import com.kuo.urcoco.common.item.CurrentAccountData;

import java.util.ArrayList;

/*
 * Created by Kuo on 2015/11/13.
 */
public class SpinnerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> strings = new ArrayList<>();

    private int curItemId = 0;

    public SpinnerAdapter(Context context, ArrayList<String> strings) {
        this.context = context;
        this.strings = strings;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int position) {
        return strings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        SpinnerViewHolder viewHolder;

        if (convertView == null || !convertView.getTag().toString().equals("DROPDOWN")) {

            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
            viewHolder = new SpinnerViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (SpinnerViewHolder) convertView.getTag();


        viewHolder.textView.setBackgroundColor(ContextCompat.getColor(context, R.color.Grey_50));
        viewHolder.textView.setTextColor(ContextCompat.getColor(context, R.color.Grey_800));
        viewHolder.textView.setText(strings.get(position));

        if(curItemId == position)
            viewHolder.textView.setTextColor(CurrentAccountData.getAccountItem().getColor());

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SpinnerViewHolder viewHolder;

        if (convertView == null ) { //if ... || !convertView.getTag().toString().equals("NON_DROPDOWN")

            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
            //convertView.setTag("NON_DROPDOWN");

            viewHolder = new SpinnerViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(viewHolder);

        } else
            viewHolder = (SpinnerViewHolder) convertView.getTag();


        viewHolder.textView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        viewHolder.textView.setTextColor(Color.parseColor("#FFFFFF"));

        if(position == getCount() - 1) {
            viewHolder.textView.setText(strings.get(curItemId));
        } else {
            viewHolder.textView.setText(strings.get(position));
        }

        return convertView;
    }

    public void setCurItemId(int curItemId) {
        this.curItemId = curItemId;
    }

    private static class SpinnerViewHolder {
        TextView textView;
    }

}
