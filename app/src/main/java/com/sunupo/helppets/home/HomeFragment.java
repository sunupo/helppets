package com.sunupo.helppets.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunupo.helppets.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomePageAdapter homePageAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

//    在创建Fragment的时候，给Fragment传递参数Bundle
    public static HomeFragment newInstance(Bundle bundle){
        HomeFragment homeFragment =new HomeFragment();
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 3/15/2019
        Bundle bundle;
        bundle=getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        return view;
    }
    private void initView(View view){
        tabLayout=view.findViewById(R.id.home_tab);

        viewPager=view.findViewById(R.id.home_viewpager);
        homePageAdapter=new HomePageAdapter(getChildFragmentManager(),getFragmentList());
        viewPager.setAdapter(homePageAdapter);
//        这样添加tablayout的标题和图片好像不行，只有在homePageAdapter设置了
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_launcher_square).setText("收藏"),0,false);
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_launcher_square).setText("广场"),1,true);
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_launcher_square).setText("同城"),2,false);
        tabLayout.setupWithViewPager(viewPager,true);
    }
    public List<Fragment> getFragmentList() {

        List<Fragment> fragmentList = new ArrayList<>();


        HomeItemCollectionFragment collectionFragment = new HomeItemCollectionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("TEST_PARA", 1);
        collectionFragment.setArguments(bundle);

        HomeItemSquareFragment squareFragment = new HomeItemSquareFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("TEST_PARA", 2);
        squareFragment.setArguments(bundle2);

        HomeItemLocalFragment localFragment = new HomeItemLocalFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt("TEST_PARA", 3);
        localFragment.setArguments(bundle3);

        fragmentList.add(collectionFragment);
        fragmentList.add(squareFragment);
        fragmentList.add(localFragment);


        return fragmentList;
    }
}
