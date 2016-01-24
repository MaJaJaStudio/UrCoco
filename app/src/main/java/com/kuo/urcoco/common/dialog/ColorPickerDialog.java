package com.kuo.urcoco.common.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;

import com.kuo.urcoco.R;
import com.kuo.urcoco.common.ColorCircle;
import com.kuo.urcoco.common.WrapContentHeightGridView;
import com.kuo.urcoco.common.adapter.ColorCircleAdapter;

import java.util.ArrayList;

/**
 * Created by User on 2015/11/30.
 */
public class ColorPickerDialog extends DialogFragment {

    private int focus = 0;
    private int color;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_color_picker, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        initGridView(view);
        initButton(view);

        return view;
    }

    private Handler handler = new Handler();

    private void initGridView(View view) {

        WrapContentHeightGridView gridView = (WrapContentHeightGridView) view.findViewById(R.id.grid_view);

        ArrayList<Integer> colors = new ArrayList<>();

        int[] typeColors = {R.color.Red_500, R.color.Pink_500, R.color.Purple_500, R.color.DeepPurple_500,
                R.color.Indigo_500, R.color.Blue_500, R.color.LightBlue_500, R.color.Cyan_500, R.color.Teal_500,
                R.color.Green_500, R.color.LightGreen_500, R.color.Lime_500, R.color.Yellow_500, R.color.Amber_500,
                R.color.Orange_500, R.color.DeepOrange_500, R.color.Brown_500, R.color.Grey_500, R.color.BlueGrey_500};

        for(int i = 0 ; i < typeColors.length ; i++) {
            colors.add(ContextCompat.getColor(getContext(), typeColors[i]));
        }

        final ColorCircleAdapter colorCircleAdapter = new ColorCircleAdapter(getContext(), colors);

        gridView.setAdapter(colorCircleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (colorCircleAdapter.getFocusId() != -1) {
                    if(parent.getChildAt(colorCircleAdapter.getFocusId()) != null) {
                        ColorCircle lastColorCircle = (ColorCircle) parent.getChildAt(colorCircleAdapter.getFocusId()).findViewById(R.id.colorCircle);
                        lastColorCircle.cancelAnimation();
                    }
                }

                ColorCircle colorCircle = (ColorCircle) view.findViewById(R.id.colorCircle);
                colorCircle.startAnimation();

                colorCircleAdapter.setFocusId(position);
                color = colorCircleAdapter.getColor(position);


            }
        });
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
            switch (v.getId()) {
                case R.id.cancelButton:
                    getDialog().dismiss();
                    break;
                case R.id.enterButton:
                    if(onEnterClickListener != null) {
                        onEnterClickListener.onClick(color);
                    }
                    getDialog().dismiss();
                    break;
            }
        }
    };

    private OnEnterClickListener onEnterClickListener;

    public interface OnEnterClickListener {
        void onClick(int color);
    }

    public void setOnEnterClickListener(OnEnterClickListener onEnterClickListener) {
        this.onEnterClickListener = onEnterClickListener;
    }

}
