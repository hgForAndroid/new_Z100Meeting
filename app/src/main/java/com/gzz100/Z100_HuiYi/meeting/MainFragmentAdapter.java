package com.gzz100.Z100_HuiYi.meeting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by XieQXiong on 2016/8/23.
 */
public class MainFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;
    public MainFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        Log.e("instantiateItem ==>","position = "+position);
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        Log.e("destroyItem ==>","position = "+position);
        super.destroyItem(container, position, object);
    }
}
