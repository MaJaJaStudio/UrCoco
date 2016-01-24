package com.kuo.urcoco.common.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kuo.urcoco.R;
import com.kuo.urcoco.common.CircleTextView;
import com.kuo.urcoco.common.item.MoneyItem;

import java.util.ArrayList;

/*
 * Created by Kuo on 2015/10/23.
 */
public class MoneyListAdapter extends RecyclerView.Adapter<MoneyListAdapter.MoneyViewHolder> {

    private ArrayList<MoneyItem> moneyItems = new ArrayList<>();
    private OnClickListener onClickListenerAdapter;
    private boolean deleteMode = false;
    private int RedColor = Color.parseColor("#EF5350");
    private int GreenColor = Color.parseColor("#66BB6A");

    public interface OnClickListener {
        void onClick(View view, MoneyItem moneyItem);
        void onLongClick(MoneyItem moneyItem);
        void onDeleteMode(boolean deleteSwitch);
    }

    public MoneyListAdapter(ArrayList<MoneyItem> moneyItems) {
        this.moneyItems = moneyItems;
    }

    @Override
    public MoneyListAdapter.MoneyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.money_recycler_item, parent, false);

        return new MoneyViewHolder(view);
    }

    private int deleteCount = 0;

    @Override
    public void onBindViewHolder(final MoneyListAdapter.MoneyViewHolder holder, final int position) {

        holder.iconImage.setText(String.valueOf(moneyItems.get(position).getTitleText().charAt(0)));
        holder.iconImage.setTextColor(Color.parseColor("#FFFFFF"));
        holder.iconImage.setCircleColor(moneyItems.get(position).getColor());

        holder.titleText.setText(moneyItems.get(position).getTitleText());
        holder.costText.setText("$ " + moneyItems.get(position).getCost());

        if(moneyItems.get(position).getMONEY_TYPE().equals("expense"))
            holder.costText.setTextColor(RedColor);
        else
            holder.costText.setTextColor(GreenColor);


        holder.infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!deleteMode)
                    onClickListenerAdapter.onClick(view, moneyItems.get(position));
                else {
                    if (moneyItems.get(position).getCheck()) {
                        deleteCount--;
                        startDeleteAnimator(view, position, false, 0, 0);

                        if (deleteCount == 0) {
                            deleteMode = false;
                            onClickListenerAdapter.onDeleteMode(deleteMode);
                        }
                    } else {
                        deleteCount++;
                        startDeleteAnimator(view, position, true, R.mipmap.ic_done_white_24dp, 360);
                    }
                }
            }
        });

        holder.infoLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(!deleteMode) {

                    deleteCount++;
                    deleteMode = true;
                    onClickListenerAdapter.onDeleteMode(deleteMode);
                    onClickListenerAdapter.onLongClick(moneyItems.get(position));

                    startDeleteAnimator(v, position, true, R.mipmap.ic_done_white_24dp, 360);

                    return true;
                } else {
                    return false;
                }

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.iconImage.setTransitionName("iconImage" + moneyItems.get(position).getYear()
                    + moneyItems.get(position).getMonth() + moneyItems.get(position).getDay() + position);
        }
    }

    private void startDeleteAnimator(View view, int position, boolean check, int icon, int rotation) {

        CircleTextView iconImage = (CircleTextView) view.findViewById(R.id.iconImage);

        if (icon == 0) {
            iconImage.setText(String.valueOf(moneyItems.get(position).getTitleText().charAt(0)));
            iconImage.setCircleColor(moneyItems.get(position).getColor());
        }
        else {
            iconImage.setText("");
            ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{ContextCompat.getColor(view.getContext(), R.color.BlueGrey_500)});
            iconImage.setBackgroundResource(icon);
            iconImage.setBackgroundTintList(csl);
            iconImage.setCircleColor(Color.TRANSPARENT);
        }

        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(iconImage);;
        viewPropertyAnimatorCompat.rotationY(rotation).start();

        moneyItems.get(position).setCheck(check);
    }

    @Override
    public int getItemCount() {
        return moneyItems.size();
    }

    public void setOnClickListener(OnClickListener onClickListenerAdapter) {
        this.onClickListenerAdapter = onClickListenerAdapter;
    }

    public ArrayList<MoneyItem> getMoneyItems() {
        return moneyItems;
    }

    public void setDeleteMode(boolean deleteMode) {
        this.deleteMode = deleteMode;
    }

    public class MoneyViewHolder extends RecyclerView.ViewHolder {

        public CircleTextView iconImage;
        public TextView titleText, costText;
        public RelativeLayout infoLayout;

        public MoneyViewHolder(View itemView) {
            super(itemView);

            iconImage = (CircleTextView) itemView.findViewById(R.id.iconImage);
            titleText = (TextView) itemView.findViewById(R.id.titleText);
            costText = (TextView) itemView.findViewById(R.id.costText);
            infoLayout = (RelativeLayout) itemView.findViewById(R.id.infoLayout);

        }

    }
}
