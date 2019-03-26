package com.sunupo.helppets.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.sunupo.helppets.R;
import com.sunupo.helppets.user.MineItemDynamicFragment;
import com.sunupo.helppets.user.MineItemFavoriteFragment;
import com.sunupo.helppets.user.MineItemFollowFragment;
import com.sunupo.helppets.user.MinePageAdapter;
import com.sunupo.helppets.util.App;
import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.util.DownloadImageTask;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MineFragment extends Fragment {

    private final String TAG="MineFragment";
    private MinePageAdapter minePageAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ImageView userLogo;
    private TextView userLoginName,userUserId,userState,joinDays
            ,hisFan,hisFollow,hisFavorite,hisCollect;

    LinearLayoutCompat fromApplyLayout,toApplyLayout;

    private Bundle bundle;//

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String[] str=((String)(msg.obj)).split("-");
                    hisFan.setText(str[0]+"");
                    hisFollow.setText(str[1]+"");
                    hisFavorite.setText(str[2]+"");
                    hisCollect.setText(str[3]+"");
                    break;
            }
        }
    };

    private void allBind(View view){
        userLogo=view.findViewById(R.id.user_logo);
        userLoginName=view.findViewById(R.id.user_login_name);
        userUserId=view.findViewById(R.id.user_user_id);
        userState=view.findViewById(R.id.user_state);
        joinDays=view.findViewById(R.id.join_days);
        hisFan=view.findViewById(R.id.his_fan);
        hisFollow=view.findViewById(R.id.his_follow);
        hisFavorite=view.findViewById(R.id.his_favorite);
        hisCollect=view.findViewById(R.id.his_collect);

        fromApplyLayout=view.findViewById(R.id.from_apply_layout);
        toApplyLayout=view.findViewById(R.id.to_apply_layout);

        fromApplyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),FromApplyActivity.class);
                startActivity(intent);
            }
        });
        toApplyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),ToApplyActivity.class);
                startActivity(intent);
            }
        });

        new DownloadImageTask(userLogo).execute(Constants.httpip+"/"+ App.loginUserInfo.getLogo());
        userLoginName.setText(App.loginUserInfo.getLoginName());
        userUserId.setText("ID:"+App.loginUserInfo.getUserId()+"");
        userState.setText(App.loginUserInfo.getIsBanned().equals("是")?"禁言":"正常");
        joinDays.setText(App.loginUserInfo.getLoginCount()+"");

    }
    private void getFanFollowFavoriteCollect(final int dynamicUserId,final String url) {
        Log.d(TAG, "getFanFollowFavoriteCollect: ");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("dynamicUserId",dynamicUserId+"")
                            .build();//
                    Request request = new Request.Builder().url(url).post(requestBody).build();
                    Response response = client.newCall(request).execute();

                    String responseData = response.body().string();
                    Log.d(TAG, "run: "+responseData);

                    Message message=Message.obtain(handler,1,2,3,responseData);
                    message.sendToTarget();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFanFollowFavoriteCollect(App.loginUserInfo.getUserId(),Constants.httpip+"/getFanFollowFavoriteCollect");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_mine,container,false);

        // TODO: 3/25/2019 把这个消息替换为签名
        final List<String> datas = Arrays.asList("最近即将出国了", "家里面有两只金毛，一条哈士奇，没人照顾。", "想找个重庆江北区的朋友","来帮我代养一周", "谢谢！", "........");
    //SimpleMarqueeView<T>，SimpleMF<T>：泛型T指定其填充的数据类型，比如String，Spanned等
        SimpleMarqueeView<String> marqueeView = (SimpleMarqueeView)view.findViewById(R.id.simpleMarqueeView);
        SimpleMF<String> marqueeFactory = new SimpleMF(view.getContext());
        marqueeFactory.setData(datas);
        marqueeView.setMarqueeFactory(marqueeFactory);
        marqueeView.startFlipping();

        allBind(view);
        initView(view);
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


        MDynamicFragment dynamicFragment = new MDynamicFragment();

        MFollowFragment followFragment=new MFollowFragment();


        fragmentList.add(dynamicFragment);
        fragmentList.add(followFragment);



        return fragmentList;
    }
}
