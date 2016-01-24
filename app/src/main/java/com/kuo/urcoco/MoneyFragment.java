package com.kuo.urcoco;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kuo.urcoco.common.adapter.MoneyAdapter;
import com.kuo.urcoco.common.item.CurrentAccountData;
import com.kuo.urcoco.common.item.MoneyItem;
import com.kuo.urcoco.presenter.money.FindCostOfMonthPresenter;
import com.kuo.urcoco.presenter.money.FindCostOfMonthPresenterImpl;
import com.kuo.urcoco.presenter.money.FindMoneyOfRangeDatePresenter;
import com.kuo.urcoco.presenter.money.FindMoneyOfRangeDatePresenterImpl;
import com.kuo.urcoco.view.money.FindCostOfMonthView;
import com.kuo.urcoco.view.money.FindMoneyItemsView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by User on 2016/1/9.
 */
public class MoneyFragment extends Fragment implements FindMoneyItemsView, FindCostOfMonthView {

    private View rootView;

    private FindMoneyOfRangeDatePresenter findMoneyOfRangeDatePresenter;
    private FindCostOfMonthPresenter findCostOfMonthPresenter;

    private MoneyListHandler mHandler = new MoneyListHandler(this);

    private MoneyAdapter moneyAdapter;

    private String startDate, endDate;

    public static MoneyFragment newIntance(String startDate, String endDate) {

        MoneyFragment moneyFragment = new MoneyFragment();

        Bundle bundle = new Bundle();
        bundle.putString("startDate", startDate);
        bundle.putString("endDate", endDate);
        moneyFragment.setArguments(bundle);

        return moneyFragment;
    }

    /**
     * Override Method
     * */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(rootView == null){

            rootView=inflater.inflate(R.layout.fragment_money, container, false);

            findMoneyOfRangeDatePresenter = new FindMoneyOfRangeDatePresenterImpl(this);
            findCostOfMonthPresenter = new FindCostOfMonthPresenterImpl(this);

            startDate = getArguments().getString("startDate");
            endDate = getArguments().getString("endDate");

        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onFoundMoneyItems(ArrayList<MoneyItem> moneyItems, int totalMoney) {

        moneyAdapter = new MoneyAdapter(moneyItems);
        moneyAdapter.setOnClickListener(new MoneyAdapter.OnClickListener() {
            @Override
            public void onClick(View view, MoneyItem moneyItem) {

                Intent intent = new Intent();
                intent.putExtra("mMoneyItem", moneyItem);
                intent.setClass(getActivity(), MoneyDetailActivity.class);
                getActivity().startActivity(intent);

            }

            @Override
            public void onLongClick(MoneyItem moneyItem) {

            }

            @Override
            public void onDeleteMode(boolean deleteSwitch) {

                MainActivity mainActivity = (MainActivity) getActivity();

                if (deleteSwitch) {
                    mainActivity.onOpenDeleteActionMode();
                } else {
                    mainActivity.onCloseDeleteActionMode();
                }
            }
        });
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(moneyAdapter);
    }

    @Override
    public void onFoundCostOfMonth(int costOfMonth) {

        TextView totalText = (TextView) rootView.findViewById(R.id.totalText);
        TextView budgetText = (TextView) rootView.findViewById(R.id.budgetText);
        TextView costText = (TextView) rootView.findViewById(R.id.costOfMonthText);

        int budget = CurrentAccountData.getBudget();
        int totalMoney = budget - costOfMonth;

        budgetText.setText(String.valueOf(budget));
        costText.setText(String.valueOf(costOfMonth));
        totalText.setText(String.valueOf(totalMoney));
    }

    public void updateRangeDate(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        findMoneyOfRangeDatePresenter.onFindMoneyItems(getActivity(), startDate, endDate);
    }

    public void deleteMoney() {

        new AlertDialog.Builder(getActivity())
                .setMessage("您確定要刪除這些交易紀錄嗎?")
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(deleteData).start();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    public void closeDeleteMode() {
        moneyAdapter.setDeleteMode(false);
        moneyAdapter.notifyDataSetChanged();

    }

    private Runnable deleteData = new Runnable() {
        @Override
        public void run() {

            Message message = mHandler.obtainMessage(0);

            SQLiteManager sqLiteManager = new SQLiteManager(getActivity());
            sqLiteManager.onOpen(sqLiteManager.getWritableDatabase());

            Iterator<MoneyItem> iterator = moneyAdapter.getMoneyItems().iterator();

            while (iterator.hasNext()) {
                MoneyItem moneyItem = iterator.next();
                if(moneyItem.getCheck()) {
                    sqLiteManager.deleteMoneyData(CurrentAccountData.getMoneyTableName(), moneyItem.getRowId());
                    iterator.remove();
                }
            }
            sqLiteManager.close();
            mHandler.sendMessage(message);
        }
    };

    private static class MoneyListHandler extends Handler {

        private final WeakReference<MoneyFragment> mFragment;

        public MoneyListHandler(MoneyFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            MoneyFragment fragment = mFragment.get();
            if (fragment != null) {
                if(msg.what == 0) {
                    fragment.closeDeleteMode();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        findMoneyOfRangeDatePresenter.onFindMoneyItems(getActivity(), startDate, endDate);
        Calendar calendar = Calendar.getInstance();
        findCostOfMonthPresenter.onFindCostOfMonth(getActivity(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1);

    }
}
