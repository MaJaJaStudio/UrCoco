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

import java.util.Calendar;

/**
 * Created by User on 2015/11/29.
 */
public class DateRangePickerDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_month_picker, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        initView(view);

        return view;
    }

    private void initView(View view) {

        Calendar calendar = Calendar.getInstance();

        DatePicker datePicker_1 = (DatePicker) view.findViewById(R.id.datePicker_1);
        datePicker_1.updateDate(calendar.get(Calendar.YEAR)-1, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


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

                    if(onCancelClickListener != null)
                        onCancelClickListener.onClick();

                    getDialog().dismiss();
                    break;
                case R.id.enterButton:
                    if(onEnterClickListener != null) {

                        DatePicker datePicker_1 = (DatePicker) getView().findViewById(R.id.datePicker_1);
                        DatePicker datePicker_2 = (DatePicker) getView().findViewById(R.id.datePicker_2);

                        onEnterClickListener.onClick(datePicker_1.getYear(), datePicker_1.getMonth()+1, datePicker_1.getDayOfMonth(),
                                datePicker_2.getYear(), datePicker_2.getMonth()+1, datePicker_2.getDayOfMonth());
                    }
                    getDialog().dismiss();
                    break;
            }
        }
    };

    private OnEnterClickListener onEnterClickListener;
    private OnCancelClickListener onCancelClickListener;

    public interface OnEnterClickListener {
        void onClick(int year_1, int month_1, int day_1, int year_2, int month_2, int day_2);
    }

    public interface OnCancelClickListener {
        void onClick();
    }

    public void setOnEnterClickListener(OnEnterClickListener onEnterClickListener) {
        this.onEnterClickListener = onEnterClickListener;
    }

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }
}
