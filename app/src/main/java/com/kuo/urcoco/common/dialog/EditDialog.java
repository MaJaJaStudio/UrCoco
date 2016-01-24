package com.kuo.urcoco.common.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.kuo.urcoco.R;

/**
 * Created by Kuo on 2015/11/6.
 */
public class EditDialog extends DialogFragment {

    private TextView cancelText, enterText;
    private EditText editText;

    private OnEditTextListener onEditTextListener;

    public interface OnEditTextListener {
        void getText(String text);
    }

    public static EditDialog newIntance(int inputType) {

        EditDialog editDialog = new EditDialog();

        Bundle bundle = new Bundle();
        bundle.putInt("INPUT_TYPE", inputType);
        editDialog.setArguments(bundle);

        return editDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_edit, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        findView(view);
        initView();

        return view;
    }

    private void findView(View view) {

        editText = (EditText) view.findViewById(R.id.editText);
        cancelText = (TextView) view.findViewById(R.id.cancelText);
        enterText = (TextView) view.findViewById(R.id.enterText);

        if(getArguments().getInt("INPUT_TYPE", -1) != -1) {
            editText.setInputType(getArguments().getInt("INPUT_TYPE"));
        }

    }

    private void initView() {

        cancelText.setOnClickListener(onClickListener);
        enterText.setOnClickListener(onClickListener);

    }

    public void setOnEditTextListener(OnEditTextListener onEditTextListener) {
        this.onEditTextListener = onEditTextListener;
    }

    private TextView.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cancelText:
                    getDialog().dismiss();
                    break;
                case R.id.enterText:
                    if(onEditTextListener != null) {
                        onEditTextListener.getText(editText.getText().toString());
                    }
                    getDialog().dismiss();
                    break;
            }
        }
    };
}
