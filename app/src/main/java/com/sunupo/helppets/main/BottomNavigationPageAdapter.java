package com.sunupo.helppets.main;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sunupo.helppets.util.Constants;

import java.util.List;

public class BottomNavigationPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private static final String[] navigationTitle= Constants.NAVIGATION_TITLE;
    public BottomNavigationPageAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
//        用自定义Fragment的newInstance()方法返回一个实例
//        return CustomTrainingFragment.newInstance(i);

//        自定义的fragment列表
        return fragmentList.get(i);
        //return CustomTrainingFragment.newInstance(i+1);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


// 添加tab的标题title
// 如下这种使用方式好像不行
//    mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
//    TabLayout.Tab tab1 = mTabLayout.newTab()
//                //设置tab项显示的文字
//                .setText("tab1");
//     TabLayout.Tab tab2 = mTabLayout.newTab().setText("tab2");
//    TabLayout.Tab tab3 = mTabLayout.newTab().setText("tab3");
//        mTabLayout.addTab(tab1);
//        mTabLayout.addTab(tab2);
//        mTabLayout.addTab(tab3);
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return navigationTitle[position];
    }
}
