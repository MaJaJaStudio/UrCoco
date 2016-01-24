package com.kuo.urcoco;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kuo.urcoco.common.ExportDatabaseExcelTask;
import com.kuo.urcoco.common.adapter.SettingAdapter;
import com.kuo.urcoco.common.alarm.AlarmOfDay;
import com.kuo.urcoco.common.dialog.TimePickerDialog;
import com.kuo.urcoco.common.item.CurrentAccountData;
import com.kuo.urcoco.common.item.SettingItem;

import java.io.File;
import java.util.ArrayList;

/*
 * Created by Kuo on 2015/12/16.
 */
public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.settingColorPrimaryDark));
        }

        initToolbar();
        initRecyclerView();
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
            CurrentAccountData.setAccountItem(null);
            Intent intent = new Intent();
            intent.setClass(SettingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent();
        intent.setClass(SettingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("設定");
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        SettingAdapter settingAdapter = new SettingAdapter(this, getSupportFragmentManager(), getSettingItem()) ;
        settingAdapter.setOnItemClickListener(new SettingAdapter.OnItemClickListener() {
            @Override
            public void onClick(final View v, int position) {
                switch (position) {
                    case 1:

                        TimePickerDialog timePickerDialog = new TimePickerDialog();
                        timePickerDialog.setOnEnterClickListener(new TimePickerDialog.OnEnterClickListener() {
                            @Override
                            public void onClick(int hour, int minute) {

                                ((TextView) v.findViewById(R.id.date_text)).setText(hour + ":" + minute);

                                SharedPreferences sharedPreferences = getSharedPreferences("date", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("alarm_hour_time", hour);
                                editor.putInt("alarm_minute_time", minute);
                                editor.apply();

                                AlarmOfDay alarmOfDay = new AlarmOfDay(SettingActivity.this);
                                alarmOfDay.onCancelAlarm();
                                alarmOfDay.onCreateAlarm();
                            }
                        });
                        timePickerDialog.show(getSupportFragmentManager(), "dialog");
                        break;

                    case 2:
                        ExportDatabaseExcelTask exportDatabaseExcelTask = new ExportDatabaseExcelTask(SettingActivity.this);
                        exportDatabaseExcelTask.setExportDatabaseExcelTaskListener(new ExportDatabaseExcelTask.ExportDatabaseExcelTaskListener() {
                            @Override
                            public void taskComplete(String path) {
                                File file = new File(path);
                                Intent fileIntent;
                                fileIntent = new Intent(Intent.ACTION_SEND);
                                fileIntent.setType("text/*");
                                fileIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                                startActivity(fileIntent);
                            }
                        });
                        exportDatabaseExcelTask.execute();
                        break;
                    case 4:
                        Intent emailIntent;
                        emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse("mailto:k.kuanwei@gmail.com"));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "text");
                        startActivity(emailIntent);
                        break;
                    case 5:
                        Intent shareIntent;
                        shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/*");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/apps/testing/com.kuo.moneycat");
                        startActivity(shareIntent);
                        break;
                    case 7:
                        break;
                }
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(settingAdapter);
    }

    private ArrayList<SettingItem> getSettingItem() {

        ArrayList<SettingItem> settingItems = new ArrayList<>();

        String[] strings = {"一般設定", "提醒", "匯出", "支援", "協助", "分享"};
        int[] ints = {1, R.mipmap.ic_alarm_black_36dp, R.mipmap.ic_save_black_36dp, 1, R.mipmap.ic_mail_outline_black_36dp, R.mipmap.ic_share_black_36dp, 1, R.mipmap.ic_person_outline_black_36dp};

        for(int i = 0 ; i < strings.length ; i++) {
            SettingItem settingItem = new SettingItem();
            settingItem.setText(strings[i]);
            settingItem.setIconResId(ints[i]);
            settingItems.add(settingItem);
        }

        return settingItems;
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
