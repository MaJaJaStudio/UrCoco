package com.kuo.urcoco.common.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuo.urcoco.R;
import com.kuo.urcoco.common.item.TypeItem;

import java.util.ArrayList;

/*
 * Created by User on 2015/12/6.
 */

public class ChooseTypeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TypeItem> typeItems = new ArrayList<>();

    public ChooseTypeAdapter(Context context, ArrayList<TypeItem> typeItems) {
        this.context = context;
        this.typeItems = typeItems;
    }

    @Override
    public int getCount() {
        return typeItems.size();
    }

    @Override
    public Object getItem(int position) {
        return typeItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_type_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.type_icon = (ImageView) convertView.findViewById(R.id.type_icon);
            viewHolder.type_name = (TextView) convertView.findViewById(R.id.type_name);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TypeItem item = typeItems.get(position);

        viewHolder.type_icon.setImageResource(context.getResources().getIdentifier(typeItems.get(position).getTypePath(), "mipmap", context.getPackageName()));
        viewHolder.type_name.setText(item.getTypeName());
        viewHolder.type_icon.setColorFilter(item.getTypeColor(), PorterDuff.Mode.MULTIPLY);

        return convertView;
    }

    public TypeItem getTypeItem(int position) {
        return typeItems.get(position);
    }

    private static class ViewHolder {
        ImageView type_icon;
        TextView type_name;
    }
}
