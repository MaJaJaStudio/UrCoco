package com.kuo.urcoco.common.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuo.urcoco.R;
import com.kuo.urcoco.common.item.SettingItem;

import java.util.ArrayList;

/*
 * Created by Kuo on 2015/12/16.
 */
public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder>{

    private static final int TITLE_SETTING = 1;

    private ArrayList<SettingItem> settingItems = new ArrayList<>();
    private FragmentManager fragmentManager;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(View v, int position);
    }

    public SettingAdapter(Context context, FragmentManager fragmentManager, ArrayList<SettingItem> settingItems) {
        this.settingItems = settingItems;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public SettingAdapter.SettingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new SettingViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_setting, parent, false));

    }

    @Override
    public void onBindViewHolder(final SettingAdapter.SettingViewHolder holder, final int position) {

        SettingItem settingItem = settingItems.get(position);

        if(settingItem.getIconResId() == TITLE_SETTING) {
            holder.imageView.setVisibility(View.GONE);
            holder.linearLayout.setClickable(false);
            holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.activity_sub_text_size));
        } else {

            holder.imageView.setImageResource(settingItem.getIconResId());
            holder.imageView.setColorFilter(ContextCompat.getColor(context, R.color.BlueGrey_500), PorterDuff.Mode.SRC_IN);

            if(settingItem.getText().equals("提醒")) {
                String formatStr = "%02d";
                SharedPreferences sharedPreferences = context.getSharedPreferences("date", 0);

                holder.date_text.setVisibility(View.VISIBLE);
                holder.date_text.setText(String.format(formatStr, sharedPreferences.getInt("alarm_hour_time", 12)) + ":" + String.format(formatStr, sharedPreferences.getInt("alarm_minute_time", 0)));
            }

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null) {
                        onItemClickListener.onClick(v, position);
                    }
                }
            });
        }
        holder.textView.setText(settingItem.getText());
    }

    @Override
    public int getItemCount() {
        return settingItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class SettingViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView, date_text;
        public LinearLayout linearLayout;

        public SettingViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            date_text = (TextView) itemView.findViewById(R.id.date_text);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}
