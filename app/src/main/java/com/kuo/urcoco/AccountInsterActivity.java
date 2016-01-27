package com.kuo.urcoco;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuo.urcoco.common.adapter.ColorAdapter;
import com.kuo.urcoco.common.dialog.EditDialog;
import com.kuo.urcoco.common.item.AccountItem;

import java.util.ArrayList;
import java.util.Calendar;

/*
 * Created by User on 2015/11/15.
 */

public class AccountInsterActivity extends AppCompatActivity {

    private static final String DATA = "data";

    public static final int INSTER_TYPE_INSTER = 1;
    public static final int INSTER_TYPE_UPDATE = 2;

    private int INSTER_TYPE;

    private int mColorPrimary;
    private int mColorPrimaryDark;
    private int mBudget;

    private int mRowId = 0;

    private Toolbar toolbar;
    private ImageView mColorImage;
    private TextView mBudgetText, mAccountText;

    private String mAccountName = "";
    private ColorAdapter mColorAdapter;

    /**
     * Override Method
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_inster);

        INSTER_TYPE = getIntent().getIntExtra("INSTER_TYPE", -1);

        mColorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        mColorPrimaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mColorImage = (ImageView) findViewById(R.id.colorImage);

        mBudgetText = (TextView) findViewById(R.id.budgetText);
        mAccountText = (TextView) findViewById(R.id.accountText);

        initToolbar();
        initFabView();
        initAccountLayout();
        initBudgetLayout();
        initColorAdapter();

        setUpdateData();
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
            Intent intent = new Intent();
            intent.setClass(AccountInsterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent();
        intent.setClass(AccountInsterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    /**
     * Private Method
     * */

    private void initToolbar() {

        toolbar.setTitle("新增帳戶");
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null && INSTER_TYPE == INSTER_TYPE_UPDATE)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initFabView() {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteManager sqLiteManager = new SQLiteManager(AccountInsterActivity.this);
                sqLiteManager.onOpen(sqLiteManager.getWritableDatabase());

                try {

                    if (INSTER_TYPE == INSTER_TYPE_INSTER)
                        insterAccount();
                    else if (INSTER_TYPE == INSTER_TYPE_UPDATE)
                        updateAccount();

                    onBackPressed();

                } catch (Exception e) {
                    if (e.getMessage() != null) {
                        if(e.getMessage().equals("repeat"))
                            showDialog("帳號重複");
                    } else {
                        showDialog("請確認資料是否填寫完畢");
                    }
                }
            }
        });
    }

    private void initAccountLayout() {

        LinearLayout accountLayout = (LinearLayout) findViewById(R.id.accountLayout);

        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialog editDialog = EditDialog.newIntance(-1);
                editDialog.show(getSupportFragmentManager(), "dialog");
                editDialog.setOnEditTextListener(new EditDialog.OnEditTextListener() {
                    @Override
                    public void getText(String text) {
                        mAccountText.setText(text);
                        mAccountName = text;
                    }
                });
            }
        });

    }

    private void initBudgetLayout() {

        LinearLayout budgetLayout = (LinearLayout) findViewById(R.id.budgetLayout);

        budgetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialog editDialog = EditDialog.newIntance(InputType.TYPE_CLASS_NUMBER);
                editDialog.show(getSupportFragmentManager(), "dialog");
                editDialog.setOnEditTextListener(new EditDialog.OnEditTextListener() {
                    @Override
                    public void getText(String text) {
                        mBudgetText.setText(text);
                        mBudget = Integer.valueOf(text);
                    }
                });
            }
        });
    }

    private void initColorAdapter() {

        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<Integer> colorsDark = new ArrayList<>();

        int[] typeColors = {R.color.Red_500, R.color.Pink_500, R.color.Purple_500, R.color.DeepPurple_500,
                R.color.Indigo_500, R.color.Blue_500, R.color.LightBlue_500, R.color.Teal_500,
                R.color.Green_500, R.color.LightGreen_500, R.color.Lime_500, R.color.Yellow_500, R.color.Amber_500,
                R.color.Orange_500, R.color.DeepOrange_500, R.color.Brown_500, R.color.Grey_500, R.color.BlueGrey_500};


        int[] typeDarkColors = {R.color.Red_700, R.color.Pink_700, R.color.Purple_700, R.color.DeepPurple_700,
                R.color.Indigo_700, R.color.Blue_700, R.color.LightBlue_700, R.color.Teal_700,
                R.color.Green_700, R.color.LightGreen_700, R.color.Lime_700, R.color.Yellow_700, R.color.Amber_700,
                R.color.Orange_700, R.color.DeepOrange_700, R.color.Brown_700, R.color.Grey_700, R.color.BlueGrey_700};

        for(int i = 0 ; i < typeColors.length ; i++) {
            colors.add(ContextCompat.getColor(this, typeColors[i]));
            colorsDark.add(ContextCompat.getColor(this, typeDarkColors[i]));
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mColorAdapter = new ColorAdapter(this, colors, colorsDark);

        mColorAdapter.setOnItemClickListener(new ColorAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                mColorPrimary = mColorAdapter.getColor(position);
                mColorPrimaryDark = mColorAdapter.getDarkColor(position);

                toolbar.setBackgroundColor(mColorPrimary);
                mColorImage.setBackgroundColor(mColorPrimary);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    getWindow().setStatusBarColor(mColorPrimaryDark);

            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mColorAdapter);

    }

    private void showDialog(String text) {
        new AlertDialog.Builder(AccountInsterActivity.this)
                .setMessage(text)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void insterAccount() throws Exception{

        SQLiteManager sqLiteManager = new SQLiteManager(this);
        sqLiteManager.onOpen(sqLiteManager.getWritableDatabase());

        Log.d("Account", mAccountName);

        if (mAccountName.equals("") || mBudget == 0) {
            throw new Exception();
        } else if (!mAccountName.equals("") && mBudget != 0) {
            if(sqLiteManager.getSresultByName(mAccountName)) {
                throw  new Exception("repeat");
            } else {
                Calendar calendar = Calendar.getInstance();
                String mDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

                sqLiteManager.insertAccountData(mAccountName, "Money_" + mAccountName, mColorPrimary, mBudget, mDate, mColorPrimaryDark);
                sqLiteManager.onCreateMoneyTable("Money_" + mAccountName);

                SharedPreferences sharedPreferences = getSharedPreferences(DATA, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("account", mAccountName);
                editor.apply();
            }
        }

        sqLiteManager.close();
    }

    private void updateAccount() throws SQLiteException {

        SQLiteManager sqLiteManager = new SQLiteManager(this);
        sqLiteManager.onOpen(sqLiteManager.getWritableDatabase());

        if (mAccountName.equals("") || mBudget == 0) {
            throw new SQLiteException("");
        } else if (!mAccountName.equals("")) {
            sqLiteManager.updateAccountData(mRowId, mAccountName, mBudget, mColorPrimary, mColorPrimaryDark);
        }
    }

    private void setUpdateData() {

        if(INSTER_TYPE == INSTER_TYPE_UPDATE) {

            AccountItem mAccountItem = (AccountItem) getIntent().getSerializableExtra("ACCOUNT_ITEM");

            if(mAccountItem != null) {

                mColorPrimary = mAccountItem.getColor();
                mColorPrimaryDark = mAccountItem.getColorDark();

                mRowId = mAccountItem.getRowId();
                mAccountName = mAccountItem.getAccountName();
                mBudget = mAccountItem.getBudget();

                mAccountText.setText(mAccountName);
                mBudgetText.setText(String.valueOf(mBudget));

                toolbar.setBackgroundColor(mAccountItem.getColor());
                mColorImage.setBackgroundColor(mAccountItem.getColor());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(mAccountItem.getColorDark());
                }

            }
        }

    }

}
