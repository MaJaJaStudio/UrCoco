package com.kuo.urcoco.common.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/*
 * Created by Kuo on 2015/10/23.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();

    public MainPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragments, ArrayList<String> titles) {
        super(fragmentManager);

        this.fragments.clear();
        this.titles.clear();
        notifyDataSetChanged();

        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return (fragments == null || fragments.size() == 0) ? null : fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (titles.size() > position) ? titles.get(position) : "";
    }
}
