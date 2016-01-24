package com.kuo.urcoco.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuo.urcoco.R;
import com.kuo.urcoco.common.ColorCircle;

import java.util.ArrayList;

/**
 * Created by User on 2016/1/3.
 */
public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder>{

    ArrayList<Integer> colors = new ArrayList<>();
    ArrayList<Integer> colorsDark = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public ColorAdapter(Context context, ArrayList<Integer> colors, ArrayList<Integer> colorsDark) {
        this.context = context;
        this.colors = colors;
        this.colorsDark = colorsDark;
    }

    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_colorcircle_item, parent, false);

        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ColorViewHolder holder, final int position) {
        holder.colorCircle.setColor(colors.get(position));

        holder.colorCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null)
                    onItemClickListener.onClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public int getColor(int position) {
        return colors.get(position);
    }

    public int getDarkColor(int position) {
        return colorsDark.get(position);
    }

    public static class ColorViewHolder extends RecyclerView.ViewHolder {

        ColorCircle colorCircle;

        public ColorViewHolder(View itemView) {
            super(itemView);

            colorCircle = (ColorCircle) itemView.findViewById(R.id.colorCircle);
        }
    }


}
