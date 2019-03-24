package com.sunupo.helppets.Mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.sunupo.helppets.R;
import com.sunupo.helppets.user.MineItemDynamicFragment;
import com.sunupo.helppets.user.MineItemFavoriteFragment;
import com.sunupo.helppets.user.MineItemFollowFragment;
import com.sunupo.helppets.user.MinePageAdapter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MineFragment extends Fragment {


    private MinePageAdapter minePageAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_mine,container,false);

        final List<String> datas = Arrays.asList("最近即将出国了", "家里面有两只金毛，一条哈士奇，没人照顾。", "想找个重庆江北区的朋友","来帮我代养一周", "谢谢！", "........");
    //SimpleMarqueeView<T>，SimpleMF<T>：泛型T指定其填充的数据类型，比如String，Spanned等
        SimpleMarqueeView<String> marqueeView = (SimpleMarqueeView)view.findViewById(R.id.simpleMarqueeView);
        SimpleMF<String> marqueeFactory = new SimpleMF(view.getContext());
        marqueeFactory.setData(datas);
        marqueeView.setMarqueeFactory(marqueeFactory);
        marqueeView.startFlipping();
//        initView(view);
        return view;
    }
    private void initView(View view){
        tabLayout=view.findViewById(R.id.mine_tab);

        viewPager=view.findViewById(R.id.mine_view_pager);
        minePageAdapter=new MinePageAdapter(getChildFragmentManager(),getFragmentList());
        viewPager.setAdapter(minePageAdapter);
        tabLayout.setupWithViewPager(viewPager,true);
    }
    public List<Fragment> getFragmentList() {

        List<Fragment> fragmentList = new ArrayList<>();


        MineItemDynamicFragment dynamicFragment = new MineItemDynamicFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("TEST_PARA", 1);
        dynamicFragment.setArguments(bundle1);

        MineItemFavoriteFragment favoriteFragment = new MineItemFavoriteFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("TEST_PARA", 2);
        favoriteFragment.setArguments(bundle2);

        MineItemFollowFragment followFragment=new MineItemFollowFragment();


        fragmentList.add(dynamicFragment);
//        fragmentList.add(favoriteFragment);
        fragmentList.add(followFragment);



        return fragmentList;
    }
}
