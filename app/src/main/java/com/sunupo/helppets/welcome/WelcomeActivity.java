package com.sunupo.helppets.welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.sunupo.helppets.Login.LoginActivity;
import com.sunupo.helppets.R;

import com.sunupo.helppets.util.BaseActivity;
import com.sunupo.helppets.util.Constants;

import java.util.ArrayList;
import java.util.List;

import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;


public class WelcomeActivity extends BaseActivity {

    ScrollerViewPager viewPager;
    WelcomePageAdapter adapter;

    SharedPreferences sp=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("返回");
        sp=this.getSharedPreferences(Constants.LoginInfo,MODE_PRIVATE);
        if(sp.getString("FIRST_LOGIN","true").equals("false")){
            Intent intent =new Intent(WelcomeActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }


        adapter=new WelcomePageAdapter(getSupportFragmentManager(),getFragmentList());
        viewPager = (ScrollerViewPager) findViewById(R.id.view_pager);

        SpringIndicator springIndicator = (SpringIndicator) findViewById(R.id.indicator);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);

        viewPager.setAdapter(adapter);
        viewPager.fixScrollSpeed();

        // just set viewPager
        springIndicator.setViewPager(viewPager);

//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });

    }

    public List<Fragment> getFragmentList() {

        List<Fragment> fragmentList = new ArrayList<>();

        GuideFragment guideFragment=new GuideFragment();
        GuideFragment2 guideFragment2=new GuideFragment2();
        GuideFragment3 guideFragment3=new GuideFragment3();


        fragmentList.add(guideFragment);
        fragmentList.add(guideFragment2);
        fragmentList.add(guideFragment3);
        return fragmentList;
    }
//
//    private List<String> getTitles(){
//        return Lists.newArrayList("1", "2", "3", "4");
//    }
//
//    private List<Integer> getBgRes(){
//        return Lists.newArrayList(R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4);
//    }
//
//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weicome, menu);
        return true;
    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
           finish();
        }
        if(id==R.id.menu_welcome){
            Intent intent =new Intent(WelcomeActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
