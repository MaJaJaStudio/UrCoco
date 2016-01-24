package com.kuo.urcoco.common.dialog;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TimePicker;

import com.kuo.urcoco.R;

/*
 * Created by Kuo on 2015/12/17.
 */
public class TimePickerDialog extends DialogFragment {

    private TimePicker timePicker;
    private OnEnterClickListener onEnterClickListener;

    public interface OnEnterClickListener {
        void onClick(int hour, int minute);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_time_picker, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);


        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        initButton(view);

        return view;
    }

    private void initButton(View view) {

        Button cancelButton = (Button) view.findViewById(R.id.cancelButton);
        Button enterButton = (Button) view.findViewById(R.id.enterButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onEnterClickListener != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        onEnterClickListener.onClick(timePicker.getHour(), timePicker.getMinute());
                    } else {
                        onEnterClickListener.onClick(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                    }
                }
                getDialog().dismiss();
            }
        });
    }

    public void setOnEnterClickListener(OnEnterClickListener onEnterClickListener) {
        this.onEnterClickListener = onEnterClickListener;
    }
}
