package com.kuo.urcoco.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.kuo.urcoco.SQLiteManager;
import com.kuo.urcoco.common.item.CurrentAccountData;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by User on 2015/12/20.
 */
public class ExportDatabaseExcelTask extends AsyncTask<String, Void, Boolean> {

    private Context context;
    private ProgressDialog progressDialog;
    private ExportDatabaseExcelTaskListener exportDatabaseExcelTaskListener;
    private String path;

    public interface ExportDatabaseExcelTaskListener {
        void taskComplete(String path);
    }

    public void setExportDatabaseExcelTaskListener(ExportDatabaseExcelTaskListener exportDatabaseExcelTaskListener) {
        this.exportDatabaseExcelTaskListener = exportDatabaseExcelTaskListener;
    }

    public ExportDatabaseExcelTask(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("正在匯出Excel檔");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
            if (exportDatabaseExcelTaskListener != null)
                exportDatabaseExcelTaskListener.taskComplete(path);
        }

        if(!aBoolean)
            Toast.makeText(context, "匯出失敗", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected Boolean doInBackground(String... params) {

        String fileName = CurrentAccountData.getAccountName() + ".xls";

        //Saving file in external storage

        SQLiteManager sqLiteManager = new SQLiteManager(context);
        SQLiteDatabase db = sqLiteManager.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CurrentAccountData.getMoneyTableName(), null);
        //create directory if not exist

        File exportDir = new File(Environment.getExternalStorageDirectory().getPath(), "MoneyCat");

        if (!exportDir.exists())
            exportDir.mkdirs();
        //file path
        File file = new File(exportDir, fileName);
        path = file.getPath();

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet(CurrentAccountData.getAccountName(), 0);

            try {
                sheet.addCell(new Label(0, 0, "編號"));
                sheet.addCell(new Label(1, 0, "日期"));
                sheet.addCell(new Label(2, 0, "類別"));
                sheet.addCell(new Label(3, 0, "類型"));
                sheet.addCell(new Label(4, 0, "金額"));
                sheet.addCell(new Label(5, 0, "備註"));

                if (cursor.moveToFirst()) {
                    do {
                        int i = cursor.getPosition() + 1;
                        sheet.addCell(new Label(0, i, cursor.getString(0)));
                        sheet.addCell(new Label(1, i, cursor.getString(5)));
                        sheet.addCell(new Label(2, i, cursor.getString(2)));
                        sheet.addCell(new Label(3, i, cursor.getString(1)));
                        sheet.addCell(new Label(4, i, cursor.getString(3)));
                        sheet.addCell(new Label(5, i, cursor.getString(4)));
                    } while (cursor.moveToNext());
                }
                //closing cursor
                cursor.close();
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
