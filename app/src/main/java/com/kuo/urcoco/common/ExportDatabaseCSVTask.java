package com.kuo.urcoco.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.kuo.urcoco.SQLiteManager;
import com.kuo.urcoco.common.item.CurrentAccountData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * Created by User on 2015/12/20.
 */
public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {

    private Context context;
    private ProgressDialog progressDialog;

    public ExportDatabaseCSVTask(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("正在匯出Csv檔");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if(progressDialog.isShowing())
            progressDialog.dismiss();

        if(!aBoolean)
            Toast.makeText(context, "匯出失敗", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected Boolean doInBackground(String... params) {

        //File dbFile= context.getDatabasePath("MoneyCatSQLite.db");
        SQLiteManager sqLiteManager = new SQLiteManager(context);

        File exportDir = new File(Environment.getExternalStorageDirectory().getPath(), "MoneyCat");

        if (!exportDir.exists())
            exportDir.mkdirs();

        File file = new File(exportDir, CurrentAccountData.getAccountName() + ".csv");

        try {
            file.createNewFile();
            FileOutputStream os = new FileOutputStream(file);
            os.write(0xef);
            os.write(0xbb);
            os.write(0xbf);

            CSVWriter csvWrite = new CSVWriter(new OutputStreamWriter(os));
            SQLiteDatabase db = sqLiteManager.getReadableDatabase();
            Cursor monetTableCsv = db.rawQuery("SELECT * FROM " + CurrentAccountData.getMoneyTableName(),null);
            csvWrite.writeNext(monetTableCsv.getColumnNames());


            while(monetTableCsv.moveToNext()) {
                String arrStr[] ={monetTableCsv.getString(0), monetTableCsv.getString(1), monetTableCsv.getString(2), monetTableCsv.getString(3), monetTableCsv.getString(4), monetTableCsv.getString(5)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            monetTableCsv.close();

            return true;
        }
        catch(Exception sqlEx)
        {
            Log.e("Export CSV", sqlEx.getMessage(), sqlEx);
            return false;
        }
    }
}
