package com.kuo.urcoco;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kuo.urcoco.common.CircleTextView;
import com.kuo.urcoco.common.WrapContentHeightGridView;
import com.kuo.urcoco.common.adapter.ChooseTypeAdapter;
import com.kuo.urcoco.common.adapter.ViewPagerAdapter;
import com.kuo.urcoco.common.dialog.DatePickerDialog;
import com.kuo.urcoco.common.item.CurrentAccountData;
import com.kuo.urcoco.common.item.MoneyItem;
import com.kuo.urcoco.common.item.TypeItem;
import com.kuo.urcoco.presenter.type.FindMoneyTypePresenter;
import com.kuo.urcoco.presenter.type.FindMoneyTypePresenterImpl;
import com.kuo.urcoco.view.type.FindMoneyTypeView;

import java.util.ArrayList;
import java.util.Calendar;

/*
 * Created by User on 2015/12/6.
 */
public class MoneyInsterActivity extends AppCompatActivity {

    //public static final int STORAGE_TYPE_INSTER = 20151201;
    public static final int STORAGE_TYPE_UPDATE = 20151202;

    public Toolbar toolbar;
    public ImageView type_icon;
    public EditText money_edit;
    public TextView date_text, type_name;
    public LinearLayout titleLayout;
    private ImageButton type_button;

    public MoneyItem moneyItem = new MoneyItem();

    private boolean inster_lock = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_inster);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        moneyItem.setMONEY_TYPE("expense");

        initViews();
        initViewPager();
        //initDateText();
        initEditTexts();
        initInsterButton();
        //initUpdateMode();

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

    private void initViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentMoneyType.newIntance(getIntent().getStringExtra("INSTER_TYPE")));
        fragments.add(new FragmentMoneyInster());

        ArrayList<String> titles = new ArrayList<>();
        titles.add("種類");
        titles.add("進階");

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles));
    }

    private void initDateText() {

        Calendar calendar = Calendar.getInstance();

        String formatStr = "%02d";
        String month = String.format(formatStr, (calendar.get(Calendar.MONTH)+1));
        String day = String.format(formatStr, calendar.get(Calendar.DAY_OF_MONTH));

        moneyItem.setDate(calendar.get(Calendar.YEAR) + "-" + month + "-" + day);

        TextView date_text = (TextView) findViewById(R.id.date_text);

        date_text.setText(moneyItem.getDate());
    }

    private void initEditTexts() {

        money_edit = (EditText) findViewById(R.id.money_edit);
        money_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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
                if (!inster_lock) {
                    SQLiteManager sqLiteManager = new SQLiteManager(v.getContext());
                    sqLiteManager.onOpen(sqLiteManager.getWritableDatabase());

                    if (getIntent().getIntExtra("STORAGE_TYPE", -1) == STORAGE_TYPE_UPDATE) {
                        sqLiteManager.updateMoneyData(CurrentAccountData.getMoneyTableName(), moneyItem.getRowId(), moneyItem.getTitleText(), moneyItem.getMONEY_TYPE(),
                                Integer.valueOf(((EditText) findViewById(R.id.money_edit)).getText().toString()),
                                moneyItem.getContentText(), moneyItem.getDate());
                    } else {
                        sqLiteManager.insertMoneyData(CurrentAccountData.getMoneyTableName(), moneyItem.getTitleText(), moneyItem.getMONEY_TYPE(),
                                Integer.valueOf(((EditText) findViewById(R.id.money_edit)).getText().toString()),
                                moneyItem.getContentText(), moneyItem.getDate());
                    }
                    sqLiteManager.close();
                    finish();
                }
            }
        });
    }

    /*private void initUpdateMode() {

        if(getIntent().getIntExtra("STORAGE_TYPE", -1) == STORAGE_TYPE_UPDATE) {

            moneyItem = (MoneyItem) getIntent().getSerializableExtra("MoneyItem");

            SQLiteManager sqLiteManager = new SQLiteManager(this);
            sqLiteManager.onOpen(sqLiteManager.getReadableDatabase());
            Cursor cursor = sqLiteManager.getTypeDataWhereTypeName(moneyItem.getTitleText());

            ImageView type_icon = (ImageView) findViewById(R.id.type_icon);
            type_icon.setColorFilter(moneyItem.getColor(), PorterDuff.Mode.SRC_IN);
            type_icon.setImageResource(getResources().getIdentifier(cursor.getString(1), "mipmap", getPackageName()));

            TextView type_name = (TextView) findViewById(R.id.type_name);
            type_name.setText(moneyItem.getTitleText());

            EditText money_edit = (EditText) findViewById(R.id.money_edit);
            money_edit.setText(String.valueOf(moneyItem.getCost()));

            TextView date_text = (TextView) findViewById(R.id.date_text);
            date_text.setText(moneyItem.getDate());

            content_edit.setText(moneyItem.getContentText());

            if(moneyItem.getMONEY_TYPE().equals("income")) {

                TextView income_text = (TextView) findViewById(R.id.income_text);
                TextView cost_text = (TextView) findViewById(R.id.cost_text);

                income_text.setTextColor(Color.parseColor("#FFFFFF"));
                income_text.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

                cost_text.setTextColor(ContextCompat.getColor(this, R.color.Grey_800));
                cost_text.setBackgroundResource(R.drawable.button_background_selector);
            }

            cursor.close();
            sqLiteManager.close();
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.scale_bottom_right);
    }
}
