package com.kuo.cooldatepicker_library.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuo.cooldatepicker_library.R;
import com.kuo.cooldatepicker_library.item.CalendarItem;
import com.kuo.cooldatepicker_library.view.CalendarView;

import java.util.ArrayList;

/**
 * Created by User on 2015/9/30.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    private ArrayList<CalendarItem> tableLayouts = new ArrayList<>();
    private OnDateChangeListener onDateChangeListener;


    public interface OnDateChangeListener {
        void onDateChange(int year, int month, int day, String subText);
    }

    public RecyclerViewAdapter(Context context, ArrayList<CalendarItem> tableLayouts) {

        this.context = context;
        this.tableLayouts = tableLayouts;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.date_picker_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.calendarView = (CalendarView) view.findViewById(R.id.calendarView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.calendarView.setCalendar(tableLayouts.get(i).getCalendar());
        viewHolder.calendarView.setOnClickListener(onClickListener);

        //Log.d("Itme", "" + i);

    }

    @Override
    public int getItemCount() {
        return tableLayouts.size();
    }

    private CalendarView.OnClickListener onClickListener = new CalendarView.OnClickListener() {
        @Override
        public void onClick(int year, int month, int day, String subText) {

            if(onDateChangeListener != null) {
                onDateChangeListener.onDateChange(year, month, day, subText);
            }

        }
    };

    public void setOnDateChangeListener(OnDateChangeListener onDateChangeListener) {
        this.onDateChangeListener = onDateChangeListener;
    }

    public void insterCalendarItem(int index, CalendarItem calendarItem) {

        tableLayouts.add(index, calendarItem);
        notifyItemInserted(index);

    }

    public void insterCalendarItem(CalendarItem calendarItem) {

        tableLayouts.add(calendarItem);
        notifyItemInserted(getItemCount());

    }


}
