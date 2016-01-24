package com.kuo.urcoco;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuo.urcoco.common.SlidingTabLayout;
import com.kuo.urcoco.common.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.Calendar;

/*
 * Created by Kuo on 2015/11/12.
 */
public class ChartFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(rootView == null){

            rootView = inflater.inflate(R.layout.fragment_chat, container, false);

            initView(rootView);
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    private void initView(View view) {

        ArrayList<String> strings = new ArrayList<>();
        strings.add("當月");
        strings.add("尋找過去");

        Calendar calendar = Calendar.getInstance();

        String month, day;
        String formatStr = "%02d";

        month = String.format(formatStr, (calendar.get(Calendar.MONTH) + 1));
        day = String.format(formatStr, calendar.get(Calendar.DAY_OF_MONTH));


        String date_1 = calendar.get(Calendar.YEAR) + "-" + month + "-" + day;

        Log.d("date_1", date_1);

        ArrayList<Fragment> fragmentList = new ArrayList<>();
        ArrayList<String> titleList = new ArrayList<>();

        ChartChildFragment chartFragment_1 = ChartChildFragment.newIntance("支出", date_1, date_1);
        ChartChildFragment chartFragment_2 = ChartChildFragment.newIntance("收入", date_1, date_1);

        chartFragment_1.setRangeDate(date_1, date_1);
        chartFragment_2.setRangeDate(date_1, date_1);

        fragmentList.add(chartFragment_1);
        fragmentList.add(chartFragment_2);

        titleList.add("支出");
        titleList.add("收入");

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(viewPagerAdapter);

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.slidingTabLayout);
        slidingTabLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

    }

    public ChartChildFragment getViewPagerFragmentItem(int position) {

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter) viewPager.getAdapter();

        return (ChartChildFragment) viewPagerAdapter.getItem(position);
    }
}
