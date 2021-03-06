package com.sunupo.helppets.Mine;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sunupo.helppets.util.Constants;

import java.util.List;


public class MPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private static final String[] mineTab = Constants.MINE_TAB;

    public MPageAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
//        用自定义Fragment的newInstance()方法返回一个实例
//        return CustomTrainingFragment.newInstance(i);

//        自定义的fragment列表
        return fragmentList.get(i);
    }

//    @Override
//    public int getCount() {
//        return homeTab.length;
//    }


    @Override
    public int getCount() {
        return fragmentList.size();
    }

    //    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return super.getPageTitle(position);
//    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mineTab[position];
    }
}