package com.kuo.urcoco;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kuo.urcoco.common.dialog.DatePickerDialog;
import com.kuo.urcoco.common.item.MoneyItem;

/**
 * Created by User on 2016/1/27.
 */
public class FragmentMoneyInster extends Fragment implements View.OnClickListener {

    private TextView dateText;
    private EditText contentEdit;
    private ImageView close, photo;

    private Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_money_inster, container, false);

        initViews(view);
        initLinearLayout(view);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initViews(View view) {
        dateText = (TextView) view.findViewById(R.id.dateText);
        contentEdit = (EditText) view.findViewById(R.id.contentEdit);
        close = (ImageView) view.findViewById(R.id.close);
        photo = (ImageView) view.findViewById(R.id.photo);
    }

    private void initLinearLayout(View view) {
        LinearLayout dateLayout = (LinearLayout) view.findViewById(R.id.dateLayout);
        dateLayout.setOnClickListener(this);
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
