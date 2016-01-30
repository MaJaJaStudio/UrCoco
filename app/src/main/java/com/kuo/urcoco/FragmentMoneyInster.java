package com.kuo.urcoco;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuo.urcoco.common.dialog.DatePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;

/*
 * Created by User on 2016/1/27.
 */
public class FragmentMoneyInster extends Fragment implements View.OnClickListener {

    private final static int IMAGE_RESULT_CODE = 3;

    public static final int INSTER_TYPE = 0;
    public static final int INSTER_INPUT = 1;
    public static final int INSTER_UPDATE = 2;

    private TextView dateText;
    private EditText contentEdit;
    private ImageView close, photo;

    private Bitmap bitmap;

    public static final FragmentMoneyInster newIntance(int INSTER_TYPE, String contentText, String date) {
        FragmentMoneyInster fragmentMoneyInster = new FragmentMoneyInster();

        Bundle bundle = new Bundle();
        bundle.putInt("INSTER_TYPE", INSTER_TYPE);
        bundle.putString("CONTENT_TEXT", contentText);
        bundle.putString("DATE", date);
        fragmentMoneyInster.setArguments(bundle);

        return fragmentMoneyInster;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_money_inster, container, false);

        initViews(view);
        initLinearLayout(view);

        initViewData();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == IMAGE_RESULT_CODE) {
            Uri mUri = data.getData();
            ContentResolver mContentResolver = getActivity().getContentResolver();
            try {
                Bitmap sourceBitmap = BitmapFactory.decodeStream(mContentResolver.openInputStream(mUri));
                photo.setImageBitmap(getScreenBitmap(sourceBitmap, photo.getWidth(), photo.getHeight()));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                sourceBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                MoneyInsterActivity moneyInsterActivity = (MoneyInsterActivity) getActivity();
                moneyInsterActivity.moneyItem.setImage(baos.toByteArray());

            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initViews(View view) {
        dateText = (TextView) view.findViewById(R.id.dateText);
        contentEdit = (EditText) view.findViewById(R.id.contentEdit);
        close = (ImageView) view.findViewById(R.id.close);
        photo = (ImageView) view.findViewById(R.id.photo);

        close.setOnClickListener(this);
        photo.setOnClickListener(this);
    }

    private void initLinearLayout(View view) {
        LinearLayout dateLayout = (LinearLayout) view.findViewById(R.id.dateLayout);
        dateLayout.setOnClickListener(this);
    }

    private void initViewData() {

        MoneyInsterActivity moneyInsterActivity = (MoneyInsterActivity) getActivity();

        if(getArguments().getInt("INSTER_TYPE", INSTER_TYPE) == INSTER_INPUT) {

            Calendar calendar = Calendar.getInstance();

            String formatStr = "%02d";
            String month = String.format(formatStr, (calendar.get(Calendar.MONTH)+1));
            String day = String.format(formatStr, calendar.get(Calendar.DAY_OF_MONTH));

            moneyInsterActivity.moneyItem.setDate(calendar.get(Calendar.YEAR) + "-" + month + "-" + day);

            dateText.setText(calendar.get(Calendar.YEAR) + "-" + month + "-" + day);
        } else if(getArguments().getInt("INSTER_TYPE", INSTER_TYPE) == INSTER_UPDATE) {
            dateText.setText(getArguments().getString("DATE"));
            contentEdit.setText(getArguments().getString("CONTENT_TEXT"));
        }

    }

    private Bitmap getScreenBitmap(Bitmap src, int width, int height) {
        Bitmap bitmap = null;
        float mHeight;

        if(src.getWidth() > src.getHeight()) {
            mHeight = height;
            bitmap = Bitmap.createScaledBitmap(src, width, (int) mHeight, true);
        } else {
            mHeight = ((float) width / (float) src.getWidth()) * src.getHeight();
            bitmap = Bitmap.createBitmap(Bitmap.createScaledBitmap(src, width, (int) mHeight, true), 0, 0, width, height);
        }

        return bitmap;
    }

    public String getContentText() {
        return contentEdit.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dateLayout:
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.setOnEnterClickListener(new DatePickerDialog.OnEnterClickListener() {
                    @Override
                    public void onClick(String date) {
                        dateText.setText(date);
                        MoneyInsterActivity moneyInsterActivity = (MoneyInsterActivity) getActivity();
                        moneyInsterActivity.moneyItem.setDate(date);
                    }
                });
                datePickerDialog.show(getFragmentManager(), "dateDialog");
                break;
            case R.id.photo:
                Intent openImage = new Intent();
                openImage.setType("image/*");
                openImage.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(openImage, IMAGE_RESULT_CODE);
                break;
            case R.id.close:
                if(bitmap != null) {
                    bitmap = null;
                    photo.setImageBitmap(null);
                }
                break;
        }
    }

}
