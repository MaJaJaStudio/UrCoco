package com.kuo.urcoco.common.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.kuo.urcoco.R;

/**
 * Created by Kuo on 2015/11/11.
 */
public class ConfirmDialog extends DialogFragment {

    private OnEnterClickListener onEnterClickListener;

    public static ConfirmDialog newIntance(String content) {

        ConfirmDialog confirmDialog = new ConfirmDialog();

        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        confirmDialog.setArguments(bundle);

        return confirmDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_confirm, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        initContentText(view);
        initButton(view);

        return view;
    }

    private void initContentText(View view) {

        TextView contentText = (TextView) view.findViewById(R.id.contentText);
        contentText.setText(getArguments().getString("content", "Text"));

    }

    private void initButton(View view) {

        Button cancelButton = (Button) view.findViewById(R.id.cancelButton);
        Button enterButton = (Button) view.findViewById(R.id.enterButton);

        cancelButton.setOnClickListener(onClickListener);
        enterButton.setOnClickListener(onClickListener);
    }

    private Button.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.enterButton) {
                if(onEnterClickListener != null) {
                    onEnterClickListener.onClick();
                }
            }
            getDialog().dismiss();
        }
    };

    public interface OnEnterClickListener {
        void onClick();
    }

    public void setOnEnterClickListener(OnEnterClickListener onEnterClickListener) {
        this.onEnterClickListener = onEnterClickListener;
    }
}
