package com.kuo.urcoco.common.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

import com.kuo.urcoco.R;

/*
 * Created by Kuo on 2015/12/14.
 */
public class DatePickerDialog extends DialogFragment {

    private View rootView;

    private OnEnterClickListener onEnterClickListener;
    private String date;

    public interface OnEnterClickListener {
        void onClick(String date);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(rootView == null){

            rootView = inflater.inflate(R.layout.dialog_date_picker, container, false);

            initButton(rootView);
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    private void initButton(View view) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        Button cancelButton = (Button) view.findViewById(R.id.cancelButton);
        Button enterButton = (Button) view.findViewById(R.id.enterButton);

        cancelButton.setOnClickListener(onClickListener);
        enterButton.setOnClickListener(onClickListener);

    }

    private Button.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancelButton:
                    getDialog().dismiss();
                    break;
                case R.id.enterButton:
                    if(onEnterClickListener != null) {
                        DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.datePicker);

                        String formatStr = "%02d";
                        String month = String.format(formatStr, (datePicker.getMonth()+1));
                        String day = String.format(formatStr, datePicker.getDayOfMonth());

                        date = datePicker.getYear() + "-" + month + "-" + day;
                        onEnterClickListener.onClick(date);
                    }
                    getDialog().dismiss();
                    break;
            }
        }
    };

    public void setOnEnterClickListener(OnEnterClickListener onEnterClickListener) {
        this.onEnterClickListener = onEnterClickListener;
    }
}
