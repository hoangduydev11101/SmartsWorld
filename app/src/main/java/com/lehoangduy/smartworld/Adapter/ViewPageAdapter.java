package com.lehoangduy.smartworld.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Admin on 12/1/2016.
 */

public class ViewPageAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ArrayList<String> tabTitle = new ArrayList<>();

    public void addFragment (Fragment fragmentArrayList, String title){
        this.fragmentArrayList.add(fragmentArrayList);
        this.tabTitle.add(title);
    }

    public ViewPageAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return tabTitle.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle.get(position);
    }
}
