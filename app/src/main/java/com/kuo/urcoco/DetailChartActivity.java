package com.kuo.urcoco;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.kuo.urcoco.common.item.CurrentAccountData;
import com.kuo.urcoco.presenter.money.FindTypeOfMoneyDetailPreseneter;
import com.kuo.urcoco.presenter.money.FindTypeOfMoneyDetailPreseneterImpl;
import com.kuo.urcoco.view.money.FoundDetailMoneyView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Kuo on 2016/1/5.
 */
public class DetailChartActivity extends AppCompatActivity implements FoundDetailMoneyView {

    private ArrayList<Integer> mRowIds;
    private String startDate, endDate, typeName, moneyType;

    private int mColorPrimary;
    private int mColorPrimaryDark;

    private BarChart barChart;

    //private int mTotalQuantity, mTotalMoney, mMuchMoney;
    //private String mUsedToDayOfWeek;

    private FindTypeOfMoneyDetailPreseneter findTypeOfMoneyDetailPreseneter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_chart);

        Intent intent = getIntent();
        mRowIds = intent.getIntegerArrayListExtra("mRowIds");
        startDate = intent.getStringExtra("startDate");
        endDate = intent.getStringExtra("endDate");
        typeName = intent.getStringExtra("typeName");
        moneyType = intent.getStringExtra("moneyType");

        SQLiteManager sqLiteManager = new SQLiteManager(this);
        sqLiteManager.onOpen(sqLiteManager.getReadableDatabase());

        Cursor cursor = sqLiteManager.getTypeDataWhereTypeName(typeName);
        mColorPrimary = cursor.getInt(2);
        mColorPrimaryDark = cursor.getInt(3);

        findTypeOfMoneyDetailPreseneter = new FindTypeOfMoneyDetailPreseneterImpl(this);
        findTypeOfMoneyDetailPreseneter.onFind(this, moneyType, typeName, startDate, endDate, getRangeDateCount());

        initLineChart();
        initToolbar();
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(mColorPrimary);
        toolbar.setTitle(typeName);

        if(startDate.equals(endDate))
            toolbar.setSubtitle(startDate);
        else
            toolbar.setSubtitle(startDate + " ~ " + endDate);

        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(mColorPrimaryDark);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFoundTotalMoney(int totalMoney) {
        ((TextView) findViewById(R.id.total_money)).setText(String.valueOf(totalMoney));
    }

    @Override
    public void onFoundMuchMoney(int muchMoney) {
        ((TextView) findViewById(R.id.much_money)).setText(String.valueOf(muchMoney));
    }

    @Override
    public void onFoundAvgMoneyOfDay(double avgMoney) {
        ((TextView) findViewById(R.id.avg_day_money)).setText(String.valueOf(avgMoney));
    }

    @Override
    public void onFoundTotalQuantity(int totalQuantity) {
        ((TextView) findViewById(R.id.total_quantity)).setText(String.valueOf(totalQuantity));
    }

    @Override
    public void onFoundUsedToDayOfWeek(String usedToDayOfWeek) {
        ((TextView) findViewById(R.id.used_to_day)).setText(usedToDayOfWeek);
    }

    List<BarEntry> yVal = new ArrayList<>();

    private void initLineChart() {

        barChart = (BarChart) findViewById(R.id.barChart);

        Legend l = barChart.getLegend();
        l.setEnabled(false);
        l.setTextSize(0);
        l.setXEntrySpace(0);
        l.setYEntrySpace(0);

        List<String> dates = getDaysOfRange(startDate, endDate);

        BarDataSet barDataSet = new BarDataSet(yVal, "Money");
        barDataSet.setValueTextColor(Color.TRANSPARENT);
        barDataSet.setColor(mColorPrimary);

        BarData barData = new BarData(dates, barDataSet);

        barChart.setDescription("");

        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);

        barChart.getAxisRight().setEnabled(false);
        barChart.setData(barData);
        barChart.invalidate();
    }

    private int getRangeDateCount() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        Calendar computingCalendar = Calendar.getInstance();

        try {
            computingCalendar.setTime(simpleDateFormat.parse(startDate));
            startCalendar.setTime(simpleDateFormat.parse(startDate));
            endCalendar.setTime(simpleDateFormat.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int count = Math.abs(endCalendar.get(Calendar.DAY_OF_YEAR) - startCalendar.get(Calendar.DAY_OF_YEAR)) + (Math.abs(endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)) * 365) + 1;

        return count;
    }

    private ArrayList<String> getDaysOfRange(String startDate, String endDate) {

        ArrayList<String> dates  = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        Calendar computingCalendar = Calendar.getInstance();

        try {
            computingCalendar.setTime(simpleDateFormat.parse(startDate));
            startCalendar.setTime(simpleDateFormat.parse(startDate));
            endCalendar.setTime(simpleDateFormat.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int count = Math.abs(endCalendar.get(Calendar.DAY_OF_YEAR) - startCalendar.get(Calendar.DAY_OF_YEAR)) + (Math.abs(endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)) * 365) + 1;

        SQLiteManager sqLiteManager = new SQLiteManager(this);
        sqLiteManager.onOpen(sqLiteManager.getReadableDatabase());

        String formatStr = "%02d";
        String catchStr ;
        Cursor cursor = sqLiteManager.getMoneyDataWhereTypeAndRangeDate(CurrentAccountData.getMoneyTableName(), moneyType, typeName, startDate, endDate);

        for(int i = 0; i < count; i++) {
            catchStr = computingCalendar.get(Calendar.YEAR) + "-" + String.format(formatStr, (computingCalendar.get(Calendar.MONTH) + 1)) + "-" + String.format(formatStr, computingCalendar.get(Calendar.DAY_OF_MONTH));
            dates.add(catchStr);

            if(catchStr.equals(cursor.getString(5))) {
                yVal.add(new BarEntry(cursor.getInt(3), i));
            }

            computingCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dates;
    }
}
