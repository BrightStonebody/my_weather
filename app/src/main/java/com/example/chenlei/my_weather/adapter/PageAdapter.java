package com.example.chenlei.my_weather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlei on 2017/8/31.
 */

public class PageAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments=new ArrayList<>();

    public PageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    //根据阿里应用分发平台的错误
    @Override
    public void finishUpdate(ViewGroup container) {
        try{
            super.finishUpdate(container);
        } catch (NullPointerException nullPointerException){
            Log.e("错误", "finishUpdate: Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
        }
    }

}
