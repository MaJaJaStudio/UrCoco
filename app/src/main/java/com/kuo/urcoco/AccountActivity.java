package com.kuo.urcoco;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.kuo.urcoco.common.adapter.AccountAdapter;
import com.kuo.urcoco.common.dialog.ConfirmDialog;
import com.kuo.urcoco.common.item.AccountItem;
import com.kuo.urcoco.presenter.account.FindAccountPresenter;
import com.kuo.urcoco.presenter.account.FindAccountPresenterImpl;
import com.kuo.urcoco.view.account.FindAccountView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Kuo on 2015/12/16.
 */
public class AccountActivity extends AppCompatActivity implements FindAccountView {

    private FindAccountPresenter findAccountPresenter;

    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        findAccountPresenter = new FindAccountPresenterImpl(this);
        findAccountPresenter.findAccount(this);

        initToolbar();

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
            intent.setClass(AccountActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent();
        intent.setClass(AccountActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onFoundAccount(ArrayList<AccountItem> accountItems) {
        initRecyclerView(accountItems);
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("管理帳戶");
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initRecyclerView(ArrayList<AccountItem> accountItems) {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        final AccountAdapter accountAdapter = new AccountAdapter(accountItems);
        accountAdapter.setOnItemClickListener(new AccountAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), AccountInsterActivity.class);
                intent.putExtra("INSTER_TYPE", AccountInsterActivity.INSTER_TYPE_UPDATE);
                intent.putExtra("ACCOUNT_ITEM", accountAdapter.getAccountItem(position));
                startActivity(intent);
                finish();
            }

            @Override
            public void onOpenDelete() {
                onOpenDeleteActionMode();
            }

            @Override
            public void onColseDelete() {
                onCloseDeleteActionMode();
            }

        });

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(accountAdapter);


    }

    public void onOpenDeleteActionMode() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.startActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                actionMode = mode;
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
                        ConfirmDialog confirmDialog = ConfirmDialog.newIntance("您確定要刪除嗎？");
                        confirmDialog.setOnEnterClickListener(new ConfirmDialog.OnEnterClickListener() {
                            @Override
                            public void onClick() {
                                new Thread(deleteAccount).start();
                            }
                        });
                        confirmDialog.show(getSupportFragmentManager(), "confirmDialog");
                        break;
                }

                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                AccountAdapter accountAdapter = (AccountAdapter) recyclerView.getAdapter();
                accountAdapter.setDeleteMode(false);
                accountAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onCloseDeleteActionMode() {
        actionMode.finish();
    }

    private AccountActivityHandler mHandler = new AccountActivityHandler(this);

    private Runnable deleteAccount = new Runnable() {
        @Override
        public void run() {

            Message message = mHandler.obtainMessage(0);

            SQLiteManager sqLiteManager = new SQLiteManager(getApplicationContext());
            sqLiteManager.onOpen(sqLiteManager.getWritableDatabase());

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            AccountAdapter accountAdapter = (AccountAdapter) recyclerView.getAdapter();

            Iterator<AccountItem> iterator = accountAdapter.getAccountItems().iterator();

            while (iterator.hasNext()) {
                AccountItem accountItem = iterator.next();
                if(accountItem.getCheck()) {

                    SharedPreferences sharedPreferences = getSharedPreferences("data", 0);

                    if(sharedPreferences.getString("account", "").equals(accountItem.getAccountName())) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("account", "");
                        editor.apply();
                    }
                    sqLiteManager.deleteTable(accountItem.getMoneyTableName());
                    sqLiteManager.deleteAccount(accountItem.getRowId());
                    iterator.remove();
                }
            }
            mHandler.sendMessage(message);
        }
    };

    private static class AccountActivityHandler extends Handler {

        private final WeakReference<AccountActivity> mactivity;

        public AccountActivityHandler(AccountActivity activity) {
            mactivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AccountActivity activity = mactivity.get();
            if (activity != null) {
                if(msg.what == 0) {
                    RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerView);
                    AccountAdapter accountAdapter = (AccountAdapter) recyclerView.getAdapter();
                    accountAdapter.notifyDataSetChanged();
                    activity.onCloseDeleteActionMode();
                }
            }
        }
    }

}
