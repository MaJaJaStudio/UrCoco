package com.kuo.urcoco.common.adapter;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuo.urcoco.R;
import com.kuo.urcoco.common.CircleTextView;
import com.kuo.urcoco.common.item.MoneyItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/*
 * Created by User on 2015/12/27.
 */
public class MoneyAdapter extends RecyclerView.Adapter<MoneyAdapter.MoneyViewHolder>{

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

    public MoneyAdapter(ArrayList<MoneyItem> moneyItems) {
        this.moneyItems = moneyItems;
    }

    @Override
    public MoneyAdapter.MoneyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_money, parent, false);

        return new MoneyViewHolder(view);
    }

    private int deleteCount = 0;

    @Override
    public void onBindViewHolder(final MoneyAdapter.MoneyViewHolder holder, final int position) {

        holder.circleTextView.setText(String.valueOf(moneyItems.get(position).getTitleText().charAt(0)));
        holder.circleTextView.setCircleColor(moneyItems.get(position).getColor());

        holder.kindText.setText(moneyItems.get(position).getTitleText());
        holder.moneyText.setText(String.valueOf(moneyItems.get(position).getCost()));

               try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(simpleDateFormat.parse(moneyItems.get(position).getDate()));
            holder.dateText.setText(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH)+1) + "月" + calendar.get(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        moneyItems.get(position).setLayoutPosition(position);

        if(moneyItems.get(position).getMONEY_TYPE().equals("expense"))
            holder.moneyText.setTextColor(RedColor);
        else
            holder.moneyText.setTextColor(GreenColor);


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
            holder.circleTextView.setTransitionName("circleTextView" + moneyItems.get(position).getYear()
                    + moneyItems.get(position).getMonth() + moneyItems.get(position).getDay() + position);
        }
    }

    private void startDeleteAnimator(View view, int position, boolean check, int icon, int rotation) {

        CircleTextView circleTextView = (CircleTextView) view.findViewById(R.id.circleTextView);

        if (icon == 0) {
            circleTextView.setText(String.valueOf(moneyItems.get(position).getTitleText().charAt(0)));
            circleTextView.setCircleColor(moneyItems.get(position).getColor());
        }
        else {
            circleTextView.setText("✓");
            circleTextView.setCircleColor(ContextCompat.getColor(view.getContext(), R.color.BlueGrey_500));
        }

        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(circleTextView);
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
        onClickListenerAdapter.onDeleteMode(deleteMode);
    }

    public class MoneyViewHolder extends RecyclerView.ViewHolder {

        public CircleTextView circleTextView;
        public TextView kindText, dateText, moneyText;
        public LinearLayout infoLayout;

        public MoneyViewHolder(View itemView) {
            super(itemView);

            circleTextView = (CircleTextView) itemView.findViewById(R.id.circleTextView);
            kindText = (TextView) itemView.findViewById(R.id.kindText);
            dateText = (TextView) itemView.findViewById(R.id.dateText);
            moneyText = (TextView) itemView.findViewById(R.id.moneyText);

            infoLayout = (LinearLayout) itemView.findViewById(R.id.infoLayout);

        }

    }
}
