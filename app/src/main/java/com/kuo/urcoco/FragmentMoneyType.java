package com.kuo.urcoco;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.kuo.urcoco.common.WrapContentHeightGridView;
import com.kuo.urcoco.common.adapter.ChooseTypeAdapter;
import com.kuo.urcoco.common.item.TypeItem;
import com.kuo.urcoco.presenter.type.FindMoneyTypePresenter;
import com.kuo.urcoco.presenter.type.FindMoneyTypePresenterImpl;
import com.kuo.urcoco.view.type.FindMoneyTypeView;

import java.util.ArrayList;

/**
 * Created by User on 2016/1/27.
 */
public class FragmentMoneyType extends Fragment implements FindMoneyTypeView {

    private FindMoneyTypePresenter findMoneyTypePresenter;
    private View rootView;
    private int color;

    public static FragmentMoneyType newIntance(String type) {

        FragmentMoneyType fragmentMoneyType = new FragmentMoneyType();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragmentMoneyType.setArguments(bundle);

        return fragmentMoneyType;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_grid_view, container, false);

        color = ContextCompat.getColor(getActivity(), R.color.colorPrimary);

        findMoneyTypePresenter = new FindMoneyTypePresenterImpl(this);
        findMoneyTypePresenter.onFindMoneyType(getActivity(), getArguments().getString("type", "expense"));


        return rootView;
    }

    @Override
    public void onFoundMoneyType(ArrayList<TypeItem> typeItems) {
        initGridView(rootView, typeItems);
    }

    private void initGridView(View view, ArrayList<TypeItem> typeItems) {

        WrapContentHeightGridView gridView = (WrapContentHeightGridView) view.findViewById(R.id.grid_view);
        gridView.setAdapter(new ChooseTypeAdapter(getActivity(), typeItems));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TypeItem typeItem = ((ChooseTypeAdapter) parent.getAdapter()).getTypeItem(position);

                final MoneyInsterActivity moneyInsterActivity = (MoneyInsterActivity) getActivity();

                int colorFrom = color;
                int colorTo = typeItem.getTypeColor();
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        moneyInsterActivity.toolbar.setBackgroundColor((int) animator.getAnimatedValue());
                        moneyInsterActivity.titleLayout.setBackgroundColor((int) animator.getAnimatedValue());
                        getActivity().getWindow().setStatusBarColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimation.setDuration(300);
                colorAnimation.start();

                moneyInsterActivity.moneyItem.setTitleText(typeItem.getTypeName());
                moneyInsterActivity.moneyItem.setMONEY_TYPE(typeItem.getTypeKind());

                moneyInsterActivity.type_icon.setImageResource(getResources().getIdentifier(typeItem.getTypePath(), "mipmap", getActivity().getPackageName()));
                moneyInsterActivity.type_icon.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                moneyInsterActivity.type_name.setText(typeItem.getTypeName());

                color = typeItem.getTypeColor();

            }
        });
    }

}
