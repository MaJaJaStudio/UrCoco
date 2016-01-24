package com.kuo.urcoco;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
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
public class MoneyInsterActivity extends AppCompatActivity implements FindMoneyTypeView {

    //public static final int STORAGE_TYPE_INSTER = 20151201;
    public static final int STORAGE_TYPE_UPDATE = 20151202;

    private WrapContentHeightGridView grid_view;
    private ImageView type_icon;
    private EditText money_edit;
    private ViewPropertyAnimatorCompat gridViewAnimator;
    private float dY;
    private float curY;
    private float oldViewY;
    private float GridView_Y;

    private boolean firstRun = true;

    private MoneyItem moneyItem = new MoneyItem();

    private TextView date_text, type_name;

    private ImageButton type_button;
    private EditText content_edit;

    private boolean inster_lock = true;

    private FindMoneyTypePresenter findMoneyTypePresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_inster);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        moneyItem.setMONEY_TYPE("expense");

        initViews();
        initDateText();
        initEditTexts();
        initUserViews();
        initKindViews();
        initTypeButton();
        initDateLayout();
        initInsterButton();
        initUpdateMode();

        findMoneyTypePresenter = new FindMoneyTypePresenterImpl(this);
        findMoneyTypePresenter.onFindMoneyType(this, "expense");
    }

    private LinearLayout.LayoutParams maxContentEditPrarms;
    private LinearLayout.LayoutParams minContentEditPrarms;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(firstRun) {
            int screen_height = findViewById(R.id.mainLayout).getHeight();
            int content_edit_height = findViewById(R.id.content_edit).getHeight();
            int content_layout_height = findViewById(R.id.content_layout).getHeight();

            minContentEditPrarms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, content_edit_height);
            maxContentEditPrarms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screen_height - convertDpToPixel(18) - content_layout_height + content_edit_height));

            GridView_Y = grid_view.getY();
            firstRun = false;
        }
    }

    private void initViews() {
        type_name = (TextView) findViewById(R.id.type_name);
        type_icon = (ImageView) findViewById(R.id.type_icon);
        content_edit = (EditText) findViewById(R.id.content_edit);
    }

    private void initTypeGridView(ArrayList<TypeItem> typeItems) {
        grid_view = (WrapContentHeightGridView) findViewById(R.id.grid_view);
        grid_view.setAdapter(new ChooseTypeAdapter(this, typeItems));

        gridViewAnimator = ViewCompat.animate(grid_view);

        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TypeItem typeItem = ((ChooseTypeAdapter) grid_view.getAdapter()).getTypeItem(position);

                type_icon.setImageResource(getResources().getIdentifier(typeItem.getTypePath(), "mipmap", getPackageName()));
                type_icon.setColorFilter(typeItem.getTypeColor(), PorterDuff.Mode.SRC_IN);

                moneyItem.setTitleText(typeItem.getTypeName());
                moneyItem.setMONEY_TYPE(typeItem.getTypeKind());

                onCloseTypeGridView();
                setInsterLock();

                type_name.setText(moneyItem.getTitleText());
            }
        });


        grid_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        oldViewY = grid_view.getY();
                        dY = grid_view.getY() - event.getRawY();

                        break;
                    case MotionEvent.ACTION_MOVE:

                        curY = event.getRawY() + dY;

                        if (curY >= 0)
                            startGridViewAnimatorInY(curY, 0);

                        break;
                    case MotionEvent.ACTION_UP:

                        float rawY = oldViewY - curY;

                        if (curY != 0) {
                            if (rawY >= 200) {
                                startGridViewAnimatorInY(50, 200);
                            } else if (rawY <= 200) {
                                startGridViewAnimatorInY(GridView_Y, 200);
                            }
                        }

                        curY = 0f;

                        break;
                }
                return false;
            }
        });
    }

    private void initTypeButton() {

        type_button = (ImageButton) findViewById(R.id.type_button);
        type_button.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        type_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (grid_view.getVisibility() != View.VISIBLE)
                    onOpenTypeGridView();

            }
        });
    }

    private void initUserViews() {

        CircleTextView user_icon = (CircleTextView) findViewById(R.id.user_icon);
        user_icon.setText(String.valueOf(CurrentAccountData.getAccountName().charAt(0)));
        user_icon.setCircleColor(CurrentAccountData.getAccountItem().getColor());

        TextView user_name = (TextView) findViewById(R.id.user_name);
        user_name.setText(CurrentAccountData.getAccountName());

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

    private void initDateLayout() {

        LinearLayout date_layout = (LinearLayout) findViewById(R.id.date_layout);

        date_text = (TextView) findViewById(R.id.date_text);

        date_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.setOnEnterClickListener(new DatePickerDialog.OnEnterClickListener() {
                    @Override
                    public void onClick(String date) {
                        date_text.setText(date);
                        moneyItem.setDate(date);
                    }
                });
                datePickerDialog.show(getSupportFragmentManager(), "dialog");
            }
        });

    }

    String changeMode = "expense";

    private void initKindViews() {

        final TextView cost_text = (TextView) findViewById(R.id.cost_text);
        final TextView income_text = (TextView) findViewById(R.id.income_text);

        cost_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!changeMode.equals("expense")) {

                    changeMode = "expense";

                    findMoneyTypePresenter.onFindMoneyType(v.getContext(), "expense");

                    cost_text.setTextColor(Color.parseColor("#FFFFFF"));
                    cost_text.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));

                    income_text.setTextColor(ContextCompat.getColor(v.getContext(), R.color.Grey_800));
                    income_text.setBackgroundResource(R.drawable.button_background_selector);
                }
            }
        });

        income_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!changeMode.equals("income")) {

                    changeMode = "income";

                    findMoneyTypePresenter.onFindMoneyType(v.getContext(), "income");

                    income_text.setTextColor(Color.parseColor("#FFFFFF"));
                    income_text.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));

                    cost_text.setTextColor(ContextCompat.getColor(v.getContext(), R.color.Grey_800));
                    cost_text.setBackgroundResource(R.drawable.button_background_selector);
                }
            }
        });
    }

    private void initEditTexts() {

        money_edit = (EditText) findViewById(R.id.money_edit);
        money_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setInsterLock();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initInsterButton() {

        ImageButton inster_icon = (ImageButton) findViewById(R.id.inster_icon);
        inster_icon.setColorFilter(ContextCompat.getColor(this, R.color.Grey_600), PorterDuff.Mode.SRC_IN);
        inster_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!inster_lock) {
                    SQLiteManager sqLiteManager = new SQLiteManager(v.getContext());
                    sqLiteManager.onOpen(sqLiteManager.getWritableDatabase());

                    if (getIntent().getIntExtra("STORAGE_TYPE", -1) == STORAGE_TYPE_UPDATE) {
                        sqLiteManager.updateMoneyData(CurrentAccountData.getMoneyTableName(), moneyItem.getRowId(), moneyItem.getTitleText(), moneyItem.getMONEY_TYPE(),
                                Integer.valueOf(((EditText) findViewById(R.id.money_edit)).getText().toString()),
                                content_edit.getText().toString(), moneyItem.getDate());
                    } else {
                        sqLiteManager.insertMoneyData(CurrentAccountData.getMoneyTableName(), moneyItem.getTitleText(), moneyItem.getMONEY_TYPE(),
                                Integer.valueOf(((EditText) findViewById(R.id.money_edit)).getText().toString()),
                                content_edit.getText().toString(), moneyItem.getDate());
                    }
                    sqLiteManager.close();
                    finish();
                }
            }
        });
    }

    private void setInsterLock() {

        if(!money_edit.getText().toString().equals("") && !moneyItem.getMONEY_TYPE().equals("")) {
            inster_lock = false;
            ImageButton inster_icon = (ImageButton) findViewById(R.id.inster_icon);
            inster_icon.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        } else {
            ImageButton inster_icon = (ImageButton) findViewById(R.id.inster_icon);
            inster_icon.setColorFilter(ContextCompat.getColor(this, R.color.Grey_600), PorterDuff.Mode.SRC_IN);
        }
    }

    private void onOpenTypeGridView() {
        curY = 0f;
        content_edit.setLayoutParams(minContentEditPrarms);
        type_button.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        grid_view.setVisibility(View.VISIBLE);
    }

    private void onCloseTypeGridView() {
        curY = 0f;

        content_edit.setLayoutParams(maxContentEditPrarms);
        type_button.setColorFilter(ContextCompat.getColor(this, R.color.Grey_600), PorterDuff.Mode.SRC_IN);
        grid_view.setY(GridView_Y);
        grid_view.setVisibility(View.GONE);
    }

    private void initUpdateMode() {

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
    }

    private void startGridViewAnimatorInY(float y, int duration) {
        gridViewAnimator
                .y(y)
                .setDuration(duration)
                .start();
    }

    private float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    @Override
    public void onFoundMoneyType(ArrayList<TypeItem> typeItems) {
        initTypeGridView(typeItems);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.scale_bottom_right);
    }
}
