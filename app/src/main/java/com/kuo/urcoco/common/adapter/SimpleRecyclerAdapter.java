package com.kuo.urcoco.common.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kuo.urcoco.R;

import java.util.ArrayList;

/*
 * Created by Kuo on 2015/12/31.
 */
public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.SimpleRecyclerViewHolder> {

    private ArrayList<String> strings = new ArrayList<>();

    private int oldSelectedId = 0;

    private OnClickItemListener onClickItemListener;

    public interface OnClickItemListener {
        void onClick(View view, int position);
    }

    public SimpleRecyclerAdapter(ArrayList<String> strings) {
        this.strings = strings;
    }

    @Override
    public SimpleRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_simple, parent, false);

        return new SimpleRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleRecyclerViewHolder holder, final int position) {

        if(position == oldSelectedId) {
            holder.textView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimary));
        }

        holder.textView.setText(strings.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onClickItemListener != null && oldSelectedId != position) {
                    onClickItemListener.onClick(v, position);
                }

                if(onClickItemListener != null && position == getItemCount() - 1) {
                    onClickItemListener.onClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public void setOldSelectedId(int oldSelectedId) {
        this.oldSelectedId = oldSelectedId;
    }

    public int getOldSelectedId() {
        return oldSelectedId;
    }

    public static class SimpleRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public SimpleRecyclerViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }

}
