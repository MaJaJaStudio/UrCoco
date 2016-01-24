package com.kuo.urcoco.common.adapter;

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
import com.kuo.urcoco.common.item.AccountItem;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by User on 2015/11/15.
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private List<AccountItem> accountItems = new ArrayList<>();
    private int deleteCount = 0;

    private boolean deleteMode = false;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
        void onOpenDelete();
        void onColseDelete();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AccountAdapter(List<AccountItem> accountItems) {
        this.accountItems = accountItems;
    }

    @Override
    public AccountAdapter.AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_account_item, parent, false);

        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountAdapter.AccountViewHolder holder, int position) {
        setViewDate(holder, position);
    }

    @Override
    public int getItemCount() {
        return accountItems.size();
    }

    private void setViewDate(AccountViewHolder holder, final int position) {

        holder.accountText.setText(accountItems.get(position).getAccountName());
        holder.budgetText.setText("$ " + accountItems.get(position).getBudget());

        holder.iconImage.setText(String.valueOf(accountItems.get(position).getAccountName().charAt(0)));
        holder.iconImage.setCircleColor(accountItems.get(position).getColor());

        holder.infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!deleteMode) {
                    onItemClickListener.onClick(position);
                } else {

                    if (accountItems.get(position).getCheck()) {
                        deleteCount--;
                        setDeleteAccount(v, position, false, 0, 0);

                        if (deleteCount == 0) {
                            deleteMode = false;
                            onItemClickListener.onColseDelete();
                        }
                    } else {
                        deleteCount++;
                        setDeleteAccount(v, position, true, R.mipmap.ic_done_white_24dp, 360);
                    }

                }
            }
        });

        holder.infoLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!deleteMode) {

                    deleteCount++;
                    deleteMode = true;
                    setDeleteAccount(v, position, true, R.mipmap.ic_done_white_24dp, 360);

                    if (onItemClickListener != null) {
                        onItemClickListener.onOpenDelete();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void setDeleteAccount(View view, int position, boolean check, int icon, int rotation) {

        CircleTextView iconImage = (CircleTextView) view.findViewById(R.id.iconImage);

        if (icon == 0) {
            iconImage.setText(String.valueOf(accountItems.get(position).getAccountName().charAt(0)));
            iconImage.setCircleColor(accountItems.get(position).getColor());
        }
        else {
            iconImage.setText("✓");
            iconImage.setCircleColor(ContextCompat.getColor(view.getContext(), R.color.BlueGrey_500));
        }

        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(iconImage);;
        viewPropertyAnimatorCompat.rotationY(rotation).start();

        accountItems.get(position).setCheck(check);
    }

    public void setDeleteMode(boolean deleteMode) {
        this.deleteMode = deleteMode;
    }

    public List<AccountItem> getAccountItems() {
        return accountItems;
    }

    public AccountItem getAccountItem(int index) {
        return accountItems.get(index);
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder {

        public TextView accountText, budgetText;
        public CircleTextView iconImage;
        public RelativeLayout infoLayout;

        public AccountViewHolder(View itemView) {
            super(itemView);

            accountText = (TextView) itemView.findViewById(R.id.accountText);
            budgetText = (TextView) itemView.findViewById(R.id.budgetText);
            iconImage = (CircleTextView) itemView.findViewById(R.id.iconImage);
            infoLayout = (RelativeLayout) itemView.findViewById(R.id.infoLayout);
        }
    }
}

