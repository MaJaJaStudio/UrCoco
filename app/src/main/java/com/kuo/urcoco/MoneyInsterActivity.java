package com.kuo.urcoco;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kuo.urcoco.common.NonScrollingViewPager;
import com.kuo.urcoco.common.adapter.ViewPagerAdapter;
import com.kuo.urcoco.common.item.CurrentAccountData;
import com.kuo.urcoco.common.item.MoneyItem;

import java.lang.reflect.Proxy;
import java.util.ArrayList;

/*
 * Created by User on 2015/12/6.
 */
public class MoneyInsterActivity extends AppCompatActivity {

    //public static final int STORAGE_TYPE_INSTER = 20151201;
    public static final int STORAGE_TYPE_UPDATE = 20151202;

    public Toolbar toolbar;
    public ImageView type_icon;
    public EditText money_edit;
    public TextView type_name;
    public LinearLayout titleLayout;

    public MoneyItem moneyItem = new MoneyItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_inster);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initViews();
        initViewPager();
        initEditTexts();
        initInsterButton();

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

            NonScrollingViewPager viewPager = (NonScrollingViewPager) findViewById(R.id.viewPager);

            if(viewPager.getCurrentItem() == 0) {
                finish();
                overridePendingTransition(0, R.anim.scale_bottom_right);
            } else if(viewPager.getCurrentItem() == 1)  {
                viewPager.setCurrentItem(0);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("新增金額");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleLayout = (LinearLayout) findViewById(R.id.titleLayout);
        type_name = (TextView) findViewById(R.id.type_name);
        type_icon = (ImageView) findViewById(R.id.type_icon);
    }

    private void initEditTexts() {

        money_edit = (EditText) findViewById(R.id.money_edit);
        money_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    moneyItem.setCost(Integer.valueOf(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initInsterButton() {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NonScrollingViewPager viewPager = (NonScrollingViewPager) findViewById(R.id.viewPager);

                if(viewPager.getCurrentItem() == 0) {
                    try {
                        if(moneyItem.getTitleText().equals("") && moneyItem.getCost() == 0)
                            throw  new Exception("NONE");
                        else if(moneyItem.getCost() == 0)
                            throw new Exception("NONE_MONEY");
                        else if(moneyItem.getTitleText().equals(""))
                            throw new Exception("NONE_KIND");
                        else {
                            viewPager.setCurrentItem(1);

                            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                            fab.setImageResource(R.mipmap.ic_send_black_36dp);
                            fab.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);

                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(money_edit.getWindowToken(), 0);
                        }
                    }catch (Exception e) {
                        if(e.getMessage() != null) {
                            if(e.getMessage().equals("NONE"))
                                Toast.makeText(MoneyInsterActivity.this, "你似乎沒有輸入些東西。", Toast.LENGTH_SHORT).show();
                            else if(e.getMessage().equals("NONE_MONEY"))
                                Toast.makeText(MoneyInsterActivity.this, "Coco要記得呀！", Toast.LENGTH_SHORT).show();
                            else if(e.getMessage().equals("NONE_KIND"))
                                Toast.makeText(MoneyInsterActivity.this, "種類呢！", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if(viewPager.getCurrentItem() == 1) {

                    SQLiteManager sqLiteManager = new SQLiteManager(v.getContext());
                    sqLiteManager.onOpen(sqLiteManager.getWritableDatabase());

                    FragmentMoneyInster fragmentMoneyInster = (FragmentMoneyInster) ((ViewPagerAdapter) viewPager.getAdapter()).getItem(1);

                    if (getIntent().getIntExtra("INSTER_TYPE", FragmentMoneyInster.INSTER_TYPE) == FragmentMoneyInster.INSTER_UPDATE) {
                        sqLiteManager.updateMoney(
                                CurrentAccountData.getMoneyTableName(), moneyItem.getRowId(),
                                moneyItem.getTitleText(),
                                moneyItem.getMONEY_TYPE(),
                                Integer.valueOf(((EditText) findViewById(R.id.money_edit)).getText().toString()),
                                fragmentMoneyInster.getContentText(), moneyItem.getDate(), moneyItem.getImage());
                    } else {
                        sqLiteManager.insertMoney(
                                CurrentAccountData.getMoneyTableName(),
                                moneyItem.getTitleText(),
                                moneyItem.getMONEY_TYPE(),
                                Integer.valueOf(((EditText) findViewById(R.id.money_edit)).getText().toString()),
                                fragmentMoneyInster.getContentText(), moneyItem.getImage(), moneyItem.getDate());
                    }
                    sqLiteManager.close();
                    finish();
                }
            }
        });
    }

    private void initViewPager() {

        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(FragmentMoneyType.newIntance(getIntent().getStringExtra("MONEY_TYPE")));

        if(getIntent().getIntExtra("INSTER_TYPE", FragmentMoneyInster.INSTER_TYPE) == FragmentMoneyInster.INSTER_UPDATE) {

            moneyItem = (MoneyItem) getIntent().getSerializableExtra("MoneyItem");

            SQLiteManager sqLiteManager = new SQLiteManager(this);
            sqLiteManager.onOpen(sqLiteManager.getReadableDatabase());
            Cursor cursor = sqLiteManager.getTypeDataWhereTypeName(moneyItem.getTitleText());

            ImageView type_icon = (ImageView) findViewById(R.id.type_icon);
            type_icon.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
            type_icon.setImageResource(getResources().getIdentifier(cursor.getString(1), "mipmap", getPackageName()));

            TextView type_name = (TextView) findViewById(R.id.type_name);
            type_name.setText(moneyItem.getTitleText());

            EditText money_edit = (EditText) findViewById(R.id.money_edit);
            money_edit.setText(String.valueOf(moneyItem.getCost()));

            LinearLayout titleLayout = (LinearLayout) findViewById(R.id.titleLayout);
            titleLayout.setBackgroundColor(moneyItem.getColor());

            toolbar.setBackgroundColor(moneyItem.getColor());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(moneyItem.getColorPrimaryDark());
            }

            fragments.add(FragmentMoneyInster.newIntance(getIntent().getIntExtra("INSTER_TYPE", FragmentMoneyInster.INSTER_TYPE), moneyItem.getContentText(), moneyItem.getDate(), moneyItem.getImage()));

            cursor.close();
            sqLiteManager.close();
        } else {
            moneyItem.setMONEY_TYPE("expense");
            fragments.add(FragmentMoneyInster.newIntance(getIntent().getIntExtra("INSTER_TYPE", FragmentMoneyInster.INSTER_TYPE), "", "", null));
        }

        NonScrollingViewPager viewPager = (NonScrollingViewPager) findViewById(R.id.viewPager);

        ArrayList<String> titles = new ArrayList<>();
        titles.add("種類");
        titles.add("進階");

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles));
    }

    @Override
    public void onBackPressed() {

        NonScrollingViewPager viewPager = (NonScrollingViewPager) findViewById(R.id.viewPager);

        if(viewPager.getCurrentItem() == 0) {
            finish();
            overridePendingTransition(0, R.anim.scale_bottom_right);
        } else if(viewPager.getCurrentItem() == 1)  {
            viewPager.setCurrentItem(0);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setImageResource(R.mipmap.ic_arrow_forward_white_24dp);
        }
    }
}
