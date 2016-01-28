package com.kuo.urcoco.common.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.kuo.urcoco.FragmentMoneyInster;
import com.kuo.urcoco.MoneyInsterActivity;
import com.kuo.urcoco.R;

/**
 * Created by Kuo on 2016/1/27.
 */
public class InsterChoiceDialog extends DialogFragment implements Button.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dailog_inster_choice, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        initButton(view);

        return view;
    }

    private void initButton(View view) {

        Button expenseButton = (Button) view.findViewById(R.id.expenseButton);
        Button icomeButton = (Button) view.findViewById(R.id.icomeButton);

        expenseButton.setOnClickListener(this);
        icomeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.expenseButton:
                intent.setClass(getActivity(), MoneyInsterActivity.class);
                intent.putExtra("MONEY_TYPE", "expense");
                intent.putExtra("INSTER_TYPE", FragmentMoneyInster.INSTER_INPUT);
                getActivity().startActivity(intent);
                break;
            case R.id.icomeButton:
                intent.setClass(getActivity(), MoneyInsterActivity.class);
                intent.putExtra("MONEY_TYPE", "income");
                intent.putExtra("INSTER_TYPE", FragmentMoneyInster.INSTER_INPUT);
                getActivity().startActivity(intent);
                break;
        }

        dismiss();
    }
}
