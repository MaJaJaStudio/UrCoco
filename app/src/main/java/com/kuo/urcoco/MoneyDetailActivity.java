package com.kuo.urcoco;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kuo.urcoco.common.CircleTextView;
import com.kuo.urcoco.common.item.MoneyItem;

/*
 * Created by Kuo on 2016/1/8.
 */
public class MoneyDetailActivity extends AppCompatActivity {

    private int mColorPrimary;
    private int mColorPrimaryDark;

    private MoneyItem mMoneyItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_detail);

        mColorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        mColorPrimaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        mMoneyItem = (MoneyItem) getIntent().getSerializableExtra("mMoneyItem");

        if(mMoneyItem != null) {
            mColorPrimary = mMoneyItem.getColor();
            mColorPrimaryDark = mMoneyItem.getColorPrimaryDark();
        }

        initToolbar();
        initFab();
        initDetailViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("詳細");
        toolbar.setBackgroundColor(mColorPrimary);
        setSupportActionBar(toolbar);

        RelativeLayout titleChildLayout = (RelativeLayout) findViewById(R.id.titleChildLayout);
        titleChildLayout.setBackgroundColor(mColorPrimary);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(mColorPrimaryDark);

    }

    private void initFab() {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MoneyDetailActivity.this, MoneyInsterActivity.class);
                intent.putExtra("MoneyItem", mMoneyItem);
                intent.putExtra("STORAGE_TYPE", MoneyInsterActivity.STORAGE_TYPE_UPDATE);
                startActivity(intent);
            }
        });
    }

    private void initDetailViews() {

        SQLiteManager sqLiteManager = new SQLiteManager(this);
        sqLiteManager.onOpen(sqLiteManager.getReadableDatabase());

        Cursor cursor =  sqLiteManager.getTypeDataWhereTypeName(mMoneyItem.getTitleText());

        CircleTextView circleTextView = (CircleTextView) findViewById(R.id.circleTextView);
        circleTextView.setText("");
        circleTextView.setCircleColor(Color.TRANSPARENT);
        circleTextView.setImageResource(getResources().getIdentifier(cursor.getString(1), "mipmap", getPackageName()));

        TextView titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText(mMoneyItem.getTitleText());

        TextView dateText = (TextView) findViewById(R.id.dateText);
        dateText.setText(mMoneyItem.getDate());

        TextView moneyText = (TextView) findViewById(R.id.moneyText);
        moneyText.setText(String.valueOf(mMoneyItem.getCost()));

        TextView contentText = (TextView) findViewById(R.id.contentText);
        contentText.setText(mMoneyItem.getContentText());
    }
}
