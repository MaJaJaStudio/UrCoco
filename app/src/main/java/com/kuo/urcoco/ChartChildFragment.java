package com.kuo.urcoco;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.kuo.urcoco.common.MoneyItemView;
import com.kuo.urcoco.common.item.CurrentAccountData;
import com.kuo.urcoco.common.item.MoneyItem;
import com.kuo.urcoco.common.item.TypeItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/*
 * Created by Kuo on 2015/11/12.
 */
public class ChartChildFragment extends Fragment {

    private ArrayList<MoneyItem> mMoneyItems = null;
    private ArrayList<TypeItem> mTypeItems = null;
    private String startDate, endDate;

    private View rootView;
    String mType;

    public static ChartChildFragment newIntance(String centerText, String startDate, String endDate) {
        ChartChildFragment chartChildFragment = new ChartChildFragment();

        Bundle bundle = new Bundle();
        bundle.putString("centerText", centerText);
        bundle.putString("startDate", startDate);
        bundle.putString("endDate", endDate);
        chartChildFragment.setArguments(bundle);

        return chartChildFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(mMoneyItems != null)
            mMoneyItems.clear();

        if(mTypeItems != null)
            mTypeItems.clear();

        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_chart_child, container, false);
            initPieChart(rootView);
            setData(rootView);
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    public void setRangeDate(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private ArrayList<MoneyItem> getMoneyItems() {

        SQLiteManager sqLiteManager = new SQLiteManager(getActivity());
        sqLiteManager.onOpen(sqLiteManager.getWritableDatabase());

        ArrayList<MoneyItem> moneyItems = new ArrayList<>();

        Set<String> sets = new HashSet<>();
        Cursor cursor;

        if(getArguments().getString("centerText", "").equals("支出"))
            mType = "expense";
        else
            mType = "income";

        cursor = sqLiteManager.getMoneyDataRangeOfDateAndType(CurrentAccountData.getMoneyTableName(), startDate, endDate, mType);

        if(cursor != null) {
            cursor.moveToFirst();
            for(int i = 0 ; i < cursor.getCount() ; i++) {
                sets.add(cursor.getString(1));
                cursor.moveToNext();
            }
            cursor.close();
        }


        for(String s : sets) {

            int typeTotalMoney = 0;
            cursor = sqLiteManager.getMoneyDataWhereTypeAndRangeDate(CurrentAccountData.getMoneyTableName(), mType, s, startDate, endDate);

            if(cursor != null) {

                MoneyItem moneyItem = new MoneyItem();
                cursor.moveToFirst();

                for(int i = 0 ; i < cursor.getCount() ; i++) {
                    typeTotalMoney += cursor.getInt(3);
                    mRowIds.add(cursor.getInt(0));
                    cursor.moveToNext();
                }

                moneyItem.setTitleText(s);
                moneyItem.setCost(typeTotalMoney);
                moneyItem.setDay(cursor.getCount());
                moneyItems.add(moneyItem);

                cursor.close();
            }
        }
        sets.clear();
        sqLiteManager.close();

        return moneyItems;
    }

    private ArrayList<TypeItem> getTypeItems(ArrayList<MoneyItem> moneyItems) {

        ArrayList<TypeItem> typeItems = new ArrayList<>();

        SQLiteManager sqLiteManager = new SQLiteManager(getActivity());
        sqLiteManager.onOpen(sqLiteManager.getWritableDatabase());

        for(int i = 0 ; i < moneyItems.size() ; i++) {
            Cursor cursor = sqLiteManager.getTypeDataWhereTypeName(moneyItems.get(i).getTitleText());
            TypeItem typeItem = new TypeItem();
            typeItem.setTypeName(cursor.getString(1));
            typeItem.setTypeColor(cursor.getInt(2));
            typeItems.add(typeItem);
        }
        sqLiteManager.close();

        return typeItems;
    }

    private void initPieChart(View view) {

        PieChart mChart = (PieChart) view.findViewById(R.id.pieChart);

        mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(60f);
        mChart.setTransparentCircleRadius(64f);
        mChart.setDescription("");
        mChart.setDrawCenterText(true);
        mChart.setDrawHoleEnabled(true);
        mChart.setRotationAngle(90);
        mChart.setRotationEnabled(true);
        mChart.setUsePercentValues(true);
        mChart.setCenterText("%\n" + getArguments().getString("centerText", ""));
        mChart.setCenterTextSize(25);
        mChart.animateXY(500, 1000);

        Legend l = mChart.getLegend();
        l.setEnabled(false);
        l.setTextSize(0);
        l.setXEntrySpace(0);
        l.setYEntrySpace(0);
    }

    ArrayList<Integer> mRowIds = new ArrayList<>();

    private void setData(View view) {

        LinearLayout itemLayout = (LinearLayout) view.findViewById(R.id.itemLayout);
        itemLayout.removeAllViews();

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        final ArrayList<MoneyItem> moneyItems = getMoneyItems();
        ArrayList<TypeItem> typeItems = getTypeItems(moneyItems);

        for(int i = 0 ; i < moneyItems.size() ; i++) {
            yVals.add(new Entry(moneyItems.get(i).getCost(), i));
            xVals.add(moneyItems.get(i).getTitleText());
            colors.add(typeItems.get(i).getTypeColor());
            MoneyItemView moneyItemView = new MoneyItemView(getActivity());
            moneyItemView.setId(i);
            moneyItemView.setTypeName(moneyItems.get(i).getTitleText());
            moneyItemView.setTypePath(moneyItems.get(i).getIconImage());
            moneyItemView.setMoney(moneyItems.get(i).getCost());
            moneyItemView.setItemCount(moneyItems.get(i).getDay());
            moneyItemView.setCircleColor(typeItems.get(i).getTypeColor());
            moneyItemView.onCreateView();
            moneyItemView.findViewById(R.id.infoLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putIntegerArrayListExtra("mRowIds", mRowIds);
                    intent.putExtra("typeName", ((TextView) v.findViewById(R.id.titleText)).getText().toString());
                    intent.putExtra("moneyType", mType);
                    intent.putExtra("startDate", startDate);
                    intent.putExtra("endDate", endDate);
                    intent.setClass(getActivity(), DetailChartActivity.class);
                    getActivity().startActivity(intent);
                }
            });
            itemLayout.addView(moneyItemView);
        }

        PieDataSet set1 = new PieDataSet(yVals, "");
        set1.setValueTextColor(Color.parseColor("#FAFAFA"));
        set1.setValueTextSize(20f);
        set1.setColors(colors);

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        set1.setSelectionShift(px);

        PieData data = new PieData(xVals, set1);

        PieChart mChart = (PieChart) view.findViewById(R.id.pieChart);
        mChart.setData(data);
        mChart.invalidate();
    }

    public void update() {
        setData(rootView);
    }

    @Override
    public void onResume() {
        super.onResume();

        update();
    }
}
