package com.kuo.urcoco;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kuo.urcoco.common.CircleTextView;
import com.kuo.urcoco.common.HeaderAccountView;
import com.kuo.urcoco.common.adapter.SpinnerAdapter;
import com.kuo.urcoco.common.alarm.AlarmOfDay;
import com.kuo.urcoco.common.dialog.DateRangePickerDialog;
import com.kuo.urcoco.common.dialog.InsterChoiceDialog;
import com.kuo.urcoco.common.item.AccountItem;
import com.kuo.urcoco.common.item.CurrentAccountData;
import com.kuo.urcoco.presenter.account.FindAccountPresenter;
import com.kuo.urcoco.presenter.account.FindAccountPresenterImpl;
import com.kuo.urcoco.presenter.sql.CreateSQLitePresenter;
import com.kuo.urcoco.presenter.sql.CreateSQLitePresenterImpl;
import com.kuo.urcoco.view.account.FindAccountView;
import com.kuo.urcoco.view.sql.CreateSQLiteView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CreateSQLiteView, FindAccountView {

    private static final String DATA = "data";
    private static final String IS_FIRST = "isFirst";

    private static final int MAIN_FRAGMENT = 0;
    private static final int CHART_FRAGMENT = 1;

    private boolean resume = false;

    private String rangeDate;

    private ArrayList<AccountItem> accountItems;

    private TextView headerTextView;
    private LinearLayout heardLayout;
    private CircleTextView imageView;
    private ImageView arrowImage;

    private int mFragmentMode;

    private int nav_check = 0;
    private boolean navAccountGroup = false;

    private SpinnerAdapter spinnerAdapter;

    /**
     * Override Method
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences sharedPreferences = getSharedPreferences(DATA, 0);
        final SQLiteManager sqLiteManager = new SQLiteManager(this);

        try {
            mainHandler(sharedPreferences, sqLiteManager);
        } catch (Exception ex) {
            sqLiteManager.setSimpleTransactionListener(new SQLiteManager.SimpleTransactionListener() {
                @Override
                public void endTransaction() {
                    mainHandler(sharedPreferences, sqLiteManager);
                }
            });
        }

        initToolbar();
        initSpinner();
        initFab();
    }

    private void mainHandler(SharedPreferences sharedPreferences, SQLiteManager sqLiteManager) {

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        if(sharedPreferences.getBoolean(IS_FIRST, true)) {
            CreateSQLitePresenter createSQLitePresenter = new CreateSQLitePresenterImpl(MainActivity.this);
            createSQLitePresenter.createSQLite(MainActivity.this);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("alarm_hour_time", 12);
            editor.putInt("alarm_minute_time", 0);
            editor.putBoolean(IS_FIRST, false);
            editor.apply();

            AlarmOfDay alarmOfDay = new AlarmOfDay(MainActivity.this);
            alarmOfDay.onCreateAlarm();
        }

        if (!sharedPreferences.getString("account", "").equals("")) {

            Cursor cursor = sqLiteManager.getAccountWhereAccountName(sharedPreferences.getString("account", ""));

            AccountItem accountItem = new AccountItem();
            accountItem.setRowId(cursor.getInt(0));
            accountItem.setAccountName(cursor.getString(1));
            accountItem.setMoneyTableName(cursor.getString(2));
            accountItem.setColor(cursor.getInt(3));
            accountItem.setBudget(cursor.getInt(4));
            accountItem.setColorDark(cursor.getInt(6));

            CurrentAccountData.setAccountItem(accountItem);

            sqLiteManager.close();
            cursor.close();
            mFragmentMode = MAIN_FRAGMENT;

        }

        FindAccountPresenter findAccountPresenter = new FindAccountPresenterImpl(MainActivity.this);
        findAccountPresenter.findAccount(MainActivity.this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home){
            if(drawerArrowDrawable.getProgress() == 1) {
                drawerArrowDrawable.setVerticalMirror(false);
                drawerArrowDrawable.setProgress(0);

                if(getSupportActionBar() != null)
                    getSupportActionBar().setHomeAsUpIndicator(drawerArrowDrawable);

                onBackPressed();
            } else {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if(id == R.id.nav_cost && id != nav_check) {
            Calendar calendar = Calendar.getInstance();

            String formatStr = "%02d";
            String month = String.format(formatStr, (calendar.get(Calendar.MONTH)+1));
            String day = String.format(formatStr, calendar.get(Calendar.DAY_OF_MONTH));

            String date_1 = calendar.get(Calendar.YEAR) + "-" + month + "-" + day;
            String date_2 = calendar.get(Calendar.YEAR) + "-" + month + "-" + day;

            if(getSupportFragmentManager().findFragmentByTag("moneyFragment") == null) {
                MoneyFragment moneyFragment = MoneyFragment.newIntance(date_1, date_2);
                fragmentTransaction.replace(R.id.frameLayout, moneyFragment, "moneyFragment");
                fragmentTransaction.commit();
            }

            Spinner spinner_nav = (Spinner) findViewById(R.id.spinner_nav);
            spinner_nav.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT));
            ((TextView) spinner_nav.getChildAt(0)).setText("今天");

            nav_check = id;
            mFragmentMode = MAIN_FRAGMENT;

        } else if (id == R.id.nav_gallery && id != nav_check) {

            Spinner spinner_nav = (Spinner) findViewById(R.id.spinner_nav);
            spinner_nav.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT));
            ((TextView) spinner_nav.getChildAt(0)).setText("今天");

            spinnerAdapter.setCurItemId(0);

            ChartFragment chartFragment = new ChartFragment();
            fragmentTransaction.replace(R.id.frameLayout, chartFragment, "chartFragment");
            fragmentTransaction.commit();
            nav_check = id;
            mFragmentMode = CHART_FRAGMENT;

        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent();
            intent.setClass(this, SettingActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_add_account) {

            Intent intent = new Intent();
            intent.setClass(this, AccountInsterActivity.class);
            intent.putExtra("INSTER_TYPE", AccountInsterActivity.INSTER_TYPE_INSTER);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_setting_account) {
            Intent intent = new Intent();
            intent.setClass(this, AccountActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Private Method
     * */

    private NavigationView navigationView;
    private DrawerArrowDrawable drawerArrowDrawable;
    private ArrayList<HeaderAccountView> headerAccountViews = new ArrayList<>();

    private void initToolbar() {

        drawerArrowDrawable = new DrawerArrowDrawable(this);
        drawerArrowDrawable.setColor(Color.parseColor("#FFFFFF"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(drawerArrowDrawable);
        }
    }

    private void initSpinner() {
        Spinner spinner_nav = (Spinner) findViewById(R.id.spinner_nav);

        ArrayList<String> strings = new ArrayList<>();
        strings.add("今天");
        strings.add("這禮拜");
        strings.add("當月");
        strings.add("尋找過去");

        spinnerAdapter = new SpinnerAdapter(this, strings);

        spinner_nav.setOnItemSelectedListener(onItemSelectedListener);
        spinner_nav.setAdapter(spinnerAdapter);
    }

    private void initFab() {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsterChoiceDialog insterChoiceDialog = new InsterChoiceDialog();
                insterChoiceDialog.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    private void initNavViews() {

        navigationView.setCheckedItem(R.id.nav_cost);
        navigationView.setNavigationItemSelectedListener(this);

        initHeardViewChilds(navigationView.getHeaderView(0));

        nav_check = R.id.nav_cost;
    }

    private void initHeardViewChilds(View header) {

        arrowImage = (ImageView) header.findViewById(R.id.arrowImage);
        headerTextView = (TextView) header.findViewById(R.id.textView);
        imageView = (CircleTextView) header.findViewById(R.id.imageView);
        heardLayout = (LinearLayout) header.findViewById(R.id.heardLayout);

        LinearLayout accountLayout = (LinearLayout) header.findViewById(R.id.accountLayout);
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOpenAccountChildViews();
            }
        });
    }

    private void setAccountHeaderViewData(AccountItem accountItem) {

        headerTextView.setText(accountItem.getAccountName());

        imageView.setText(String.valueOf(accountItem.getAccountName().charAt(0)));
        imageView.setTextSize(25);
        imageView.setCircleColor(accountItem.getColor());

    }

    private void onOpenAccountChildViews() {

        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(arrowImage);

        if(navAccountGroup) {

            viewPropertyAnimatorCompat.rotation(0);

            navigationView.getMenu().setGroupVisible(R.id.groupNormal, true);
            navigationView.getMenu().setGroupVisible(R.id.groupAccount, false);

            heardLayout.setVisibility(View.GONE);

            navAccountGroup = false;

        } else {

            viewPropertyAnimatorCompat.rotation(180);

            navigationView.getMenu().setGroupVisible(R.id.groupNormal, false);
            navigationView.getMenu().setGroupVisible(R.id.groupAccount, true);

            heardLayout.setVisibility(View.VISIBLE);

            navAccountGroup = true;

        }

    }

    private void addAccountHeaderViews() {

        for(int i = 0 ; i < accountItems.size() ; i++) {
            if(!CurrentAccountData.getAccountName().equals(accountItems.get(i).getAccountName())) {
                addAccountHeaderChildView(accountItems.get(i));
            }
        }

        heardLayout.setVisibility(View.GONE);

    }

    private void addAccountHeaderChildView(AccountItem accountItem) {

        HeaderAccountView headerAccountView = new HeaderAccountView(this);
        headerAccountView.setAccount(accountItem);
        headerAccountView.setOnClickListener(onChangeAccounListener);
        headerAccountViews.add(headerAccountView);
        heardLayout.addView(headerAccountView);

    }

    private LinearLayout.OnClickListener onChangeAccounListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AccountItem nowAccountItem = ((HeaderAccountView) v).getAccountItem();
            AccountItem oldAccountItem = CurrentAccountData.getAccountItem();

            SharedPreferences sharedPreferences = getSharedPreferences(DATA, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("account", nowAccountItem.getAccountName());
            editor.apply();

            setAccountHeaderViewData(nowAccountItem);
            addAccountHeaderChildView(oldAccountItem);
            deleteAccountHeadWhereAccountName(nowAccountItem.getAccountName());

            CurrentAccountData.setAccountItem(nowAccountItem);

            onOpenAccountChildViews();

            String[] dates = getDateRange(0);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            MoneyFragment moneyFragment = MoneyFragment.newIntance(dates[0], dates[1]);
            fragmentTransaction.replace(R.id.frameLayout, moneyFragment, "moneyFragment");
            fragmentTransaction.commit();

            Spinner spinner_nav = (Spinner) findViewById(R.id.spinner_nav);
            ((TextView) spinner_nav.getChildAt(0)).setText("今天");
            spinnerAdapter.setCurItemId(0);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    };

    private void deleteAccountHeadWhereAccountName(String accountName) {

        Iterator<HeaderAccountView> iterator = headerAccountViews.iterator();

        while (iterator.hasNext()) {
            HeaderAccountView headerAccountView = iterator.next();
            if(headerAccountView.getAccountName().equals(accountName)) {
                heardLayout.removeView(headerAccountView);
                iterator.remove();
            }
        }
    }

    private String[] getDateRange(int index) {

        String[] dates = new String[2];
        Calendar calendar = Calendar.getInstance();
        String date_1 = "", date_2 = "";
        String month, day;
        String formatStr = "%02d";

        switch(index) {
            case 0:
                month = String.format(formatStr, (calendar.get(Calendar.MONTH)+1));
                day = String.format(formatStr, calendar.get(Calendar.DAY_OF_MONTH));

                date_1 = calendar.get(Calendar.YEAR) + "-" + month + "-" + day;
                date_2 = calendar.get(Calendar.YEAR) + "-" + month + "-" + day;
                break;
            case 1:
                calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

                month = String.format(formatStr, (calendar.get(Calendar.MONTH)+1));
                day = String.format(formatStr, calendar.get(Calendar.DAY_OF_MONTH));

                date_1 = calendar.get(Calendar.YEAR) + "-" + month + "-" + day;

                calendar.add(Calendar.DAY_OF_MONTH, 6);

                month = String.format(formatStr, (calendar.get(Calendar.MONTH)+1));
                day = String.format(formatStr, calendar.get(Calendar.DAY_OF_MONTH));

                date_2 = calendar.get(Calendar.YEAR) + "-" + month + "-" + day;
                break;
            case 2:
                calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

                month = String.format(formatStr, (calendar.get(Calendar.MONTH) + 1));
                day = String.format(formatStr, calendar.get(Calendar.DAY_OF_MONTH));

                date_1 = calendar.get(Calendar.YEAR) + "-" + month + "-01";
                date_2 = calendar.get(Calendar.YEAR) + "-" + month + "-" + day;
                break;
        }

        dates[0] = date_1;
        dates[1] = date_2;

        return dates;
    }

    /**
     * Public Method
     * */

    private ActionMode mActionMode;

    public void onOpenDeleteActionMode() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.startActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mActionMode = mode;
                mode.getMenuInflater().inflate(R.menu.delete_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                mode.setTitle("刪除");
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.meun_delete:

                        if (getSupportFragmentManager().findFragmentByTag("moneyFragment") != null) {
                            ((MoneyFragment) getSupportFragmentManager().findFragmentByTag("moneyFragment")).deleteMoney();
                        }

                        break;
                }

                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                if (getSupportFragmentManager().findFragmentByTag("moneyFragment") != null) {
                    ((MoneyFragment) getSupportFragmentManager().findFragmentByTag("moneyFragment")).closeDeleteMode();
                }
            }
        });
    }

    public void onCloseDeleteActionMode() {
        mActionMode.finish();
    }

    public void resetSpinnerItemPosition() {
        Spinner spinner_nav = (Spinner) findViewById(R.id.spinner_nav);

        if(spinner_nav.getSelectedItemPosition() == 3) {
            resume = true;
            try {
                Field field = AdapterView.class.getDeclaredField("mSelectedPosition"); //mOldSelectedPosition
                field.setAccessible(true);
                field.setInt(spinner_nav, AdapterView.INVALID_POSITION);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Spinner.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(final AdapterView<?> parent, View view, final int position, long id) {

            String date_1 = "", date_2 = "";
            String[] dates;

            if(position != 3) {

                dates = getDateRange(position);

                date_1 = dates[0];
                date_2 = dates[1];

                spinnerAdapter.setCurItemId(position);

            } else {
                Spinner spinner_nav = (Spinner) findViewById(R.id.spinner_nav);

                if(!resume) {
                    DateRangePickerDialog dateRangePickerDialog = new DateRangePickerDialog();
                    dateRangePickerDialog.setOnEnterClickListener(new DateRangePickerDialog.OnEnterClickListener() {
                        @Override
                        public void onClick(int year_1, int month_1, int day_1, int year_2, int month_2, int day_2) {
                            String date_1, date_2;

                            String formatStr = "%02d";

                            date_1 = year_1 + "-" + String.format(formatStr, month_1) + "-" + String.format(formatStr, day_1);
                            date_2 = year_2 + "-" + String.format(formatStr, month_2) + "-" + String.format(formatStr, day_2);

                            rangeDate = date_1 + "~" + date_2;

                            if (mFragmentMode == MAIN_FRAGMENT) {
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                if (getSupportFragmentManager().findFragmentByTag("moneyFragment") == null) {
                                    MoneyFragment moneyFragment = MoneyFragment.newIntance(date_1, date_2);
                                    fragmentTransaction.replace(R.id.frameLayout, moneyFragment, "moneyFragment");
                                    fragmentTransaction.commit();

                                } else {
                                    ((MoneyFragment) getSupportFragmentManager().findFragmentByTag("moneyFragment")).updateRangeDate(date_1, date_2);
                                }
                            } else if (mFragmentMode == CHART_FRAGMENT) {
                                ChartFragment chartFragment = (ChartFragment) getSupportFragmentManager().findFragmentByTag("chartFragment");
                                ChartChildFragment chartChildFragment_1 = chartFragment.getViewPagerFragmentItem(0);
                                ChartChildFragment chartChildFragment_2 = chartFragment.getViewPagerFragmentItem(1);

                                chartChildFragment_1.setRangeDate(date_1, date_2);
                                chartChildFragment_2.setRangeDate(date_1, date_2);

                                chartChildFragment_1.update();
                                chartChildFragment_2.update();
                            }

                            //Spinner spinner_nav = (Spinner) findViewById(R.id.spinner_nav);
                            parent.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT));
                            ((TextView) parent.getChildAt(0)).setText(rangeDate);

                            resetSpinnerItemPosition();
                            spinnerAdapter.setCurItemId(position);
                        }

                    });
                    dateRangePickerDialog.show(getSupportFragmentManager(), "dialog");
                } else {
                    resume = false;
                    ((TextView) parent.getChildAt(0)).setText(rangeDate);
                }

                try {
                    Field field = AdapterView.class.getDeclaredField("mOldSelectedPosition");
                    field.setAccessible(true);
                    field.setInt(spinner_nav, AdapterView.INVALID_POSITION);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if(!date_1.equals("") && !date_2.equals("")) {
                if(mFragmentMode == MAIN_FRAGMENT) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    if(getSupportFragmentManager().findFragmentByTag("moneyFragment") == null) {
                        MoneyFragment moneyFragment = MoneyFragment.newIntance(date_1, date_2);
                        fragmentTransaction.replace(R.id.frameLayout, moneyFragment, "moneyFragment");
                        fragmentTransaction.commit();

                    } else {
                        ((MoneyFragment) getSupportFragmentManager().findFragmentByTag("moneyFragment")).updateRangeDate(date_1, date_2);
                    }
                } else if(mFragmentMode == CHART_FRAGMENT) {

                    ChartFragment chartFragment = (ChartFragment) getSupportFragmentManager().findFragmentByTag("chartFragment");
                    ChartChildFragment chartChildFragment_1 = chartFragment.getViewPagerFragmentItem(0);
                    ChartChildFragment chartChildFragment_2 = chartFragment.getViewPagerFragmentItem(1);

                    chartChildFragment_1.setRangeDate(date_1, date_2);
                    chartChildFragment_2.setRangeDate(date_1, date_2);

                    chartChildFragment_1.update();
                    chartChildFragment_2.update();

                }

                Spinner spinner_nav = (Spinner) findViewById(R.id.spinner_nav);
                spinner_nav.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    /**
     * Main MVP Presenter
     * */

    @Override
    public void message(String msg, boolean i) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFoundAccount(ArrayList<AccountItem> accountItems) {

        SharedPreferences sharedPreferences = getSharedPreferences(DATA, 0);

        if (accountItems.size() == 0) {

            Intent intent = new Intent();
            intent.setClass(this, AccountInsterActivity.class);
            intent.putExtra("INSTER_TYPE", AccountInsterActivity.INSTER_TYPE_INSTER);
            startActivity(intent);
            finish();

        } else {
            if(sharedPreferences.getString("account", "").equals("")) {
                CurrentAccountData.setAccountItem(accountItems.get(0));

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("account", CurrentAccountData.getAccountName());
                editor.apply();
            }
            this.accountItems = accountItems;

            initNavViews();
            setAccountHeaderViewData(CurrentAccountData.getAccountItem());
            addAccountHeaderViews();
        }
    }
}