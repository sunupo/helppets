package com.sunupo.helppets.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.sunupo.helppets.R;
import com.sunupo.helppets.bean.DynamicBean;
import com.sunupo.helppets.util.App;
import com.sunupo.helppets.util.BaseActivity;
import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.util.DownloadImageTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//点击用户头像，查看他的主页
public class UserMainPageActivity extends BaseActivity {
    private static  final String TAG="UserMainPageActivity";
    private MinePageAdapter minePageAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    CircleImageView userLogo;
    TextView userLoginName,userUserId,userFollow,userCancelFollow,hisFan,hisFollow,hisFavorite,hisCollect;
    LinearLayoutCompat chatLayout;



    int dynamicUserId;//动态对应的userid
    int dunamicId;//动态对应的dynamicId
    Bundle bundle;//保存着具体的那一条动态的所有信息，这个bundle继续传递给fragment，来得到不同的动态、喜欢、关注等信息
    DynamicBean dynamicBean;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//       上一个界面
// Intent intent=new Intent(v.getContext(),UserMainPageActivity.class);
//        intent.putExtra("DYNAMIC_USER_ID",DYNAMIC_USER_ID);
//        intent.putExtra("DYNAMIC_ID",DYNAMIC_ID);
//        Bundle bundle=new Bundle();
//        bundle.putSerializable("DYNAMIC_BEAN",dynamicBean);
//        intent.putExtra("BUNDLE",bundle);
        Intent intent=getIntent();
        bundle=intent.getBundleExtra("BUNDLE");
        dynamicBean=(DynamicBean)(bundle.getSerializable("DYNAMIC_BEAN"));

//        http://localhost:34098/laf/getFanFollowFavoriteCollect?dynamicUserId=1
        getFanFollowFavoriteCollect(dynamicBean.getUserId(),Constants.httpip+"/getFanFollowFavoriteCollect");
        Log.d(TAG, "onCreate: ");
        final List<String> contentText=Arrays.asList(dynamicBean.getContent().split("，"));
//        final List<String> datas = Arrays.asList("最近即将出国了", "家里面有两只金毛，一跳海器，没人照顾。", "想找个重庆江北区的朋友来帮我代养一周", "谢谢！", "........");
        //SimpleMarqueeView<T>，SimpleMF<T>：泛型T指定其填充的数据类型，比如String，Spanned等
        SimpleMarqueeView<String> marqueeView = (SimpleMarqueeView)findViewById(R.id.simpleMarqueeView);
        SimpleMF<String> marqueeFactory = new SimpleMF(this);
        marqueeFactory.setData(contentText);
        marqueeView.setMarqueeFactory(marqueeFactory);
        marqueeView.startFlipping();
        initView();
//            CircleImageView userLogo;
//    TextView userLoginName,userFollow,userCancelFollow;
//    LinearLayoutCompat chatLayout;
        userLogo=findViewById(R.id.user_logo);
        userLoginName=findViewById(R.id.user_login_name);
        userUserId=findViewById(R.id.user_user_id);
        userFollow=findViewById(R.id.user_follow);
        userCancelFollow=findViewById(R.id.user_cancel_follow);
        chatLayout=findViewById(R.id.chat_layout);
//        hisFan,hisFollow,hisFavorite,hisCollect;
        hisFan=findViewById(R.id.his_fans);
        hisFollow=findViewById(R.id.his_follow);
        hisFavorite=findViewById(R.id.his_favorite);
        hisCollect=findViewById(R.id.his_collect);

        new DownloadImageTask(userLogo).execute(Constants.httpip+"/"+dynamicBean.getLogo());
        userLoginName.setText(dynamicBean.getLoginName());
        userUserId.setText("ID:"+dynamicBean.getUserId()+"");
        //todo 判断当前是否关注
        if(dynamicBean.getFollowFlag().equals("已关注")){
            userCancelFollow.setVisibility(View.VISIBLE);
            userFollow.setVisibility(View.GONE);
        }else{
            userFollow.setVisibility(View.VISIBLE);
            userCancelFollow.setVisibility(View.GONE);
        }

        userFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCancelFollow.setVisibility(View.VISIBLE);
                userFollow.setVisibility(View.GONE);
//                TODO 发送请求到服务器，关注
                Toast.makeText(v.getContext(),"关注成功",Toast.LENGTH_SHORT).show();
                sendRequestWithHttpURLConnectionFollow(App.loginUserInfo.getUserId(),dynamicBean.getUserId(),"1",Constants.httpip+"/follow");

            }
        });
        userCancelFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userFollow.setVisibility(View.VISIBLE);
                userCancelFollow.setVisibility(View.GONE);
//                TODO 发送请求到服务器，取关
                Toast.makeText(v.getContext(),"取消关注成功",Toast.LENGTH_SHORT).show();
                sendRequestWithHttpURLConnectionFollow(App.loginUserInfo.getUserId(),dynamicBean.getUserId(),"0",Constants.httpip+"/follow");
            }
        });
        chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().startPrivateChat(UserMainPageActivity.this,dynamicBean.getLoginName()+"",App.loginUserInfo.getLoginName()+"正在与"+dynamicBean.getLoginName()+"聊天");
            }
        });



    }

    /**
     * 关注取关通信函数
     * @param loginUserId
     * @param dynamicUserId
     * @param followFlag
     * @param url
     */
    private void sendRequestWithHttpURLConnectionFollow(final int loginUserId ,final int dynamicUserId
            ,final String followFlag,final String url) {
        Log.d(TAG, "sendRequestWithHttpURLConnectionFollow: "+"loginUserId="+loginUserId);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("loginUserId",loginUserId+"")
                            .add("dynamicUserId",dynamicUserId+"")
                            .add("followFlag",followFlag)
                            .build();//
                    Request request = new Request.Builder().url(url).post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    Log.d(TAG, "run:response.code= "+response.code());

                    String responseData = response.body().string();
                    Log.d(TAG, "run: "+responseData);



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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initView(){
        tabLayout=findViewById(R.id.mine_tab);

        viewPager=findViewById(R.id.mine_view_pager);
        minePageAdapter=new MinePageAdapter(getSupportFragmentManager(),getFragmentList());
        viewPager.setAdapter(minePageAdapter);
        tabLayout.setupWithViewPager(viewPager,true);
    }
    public List<Fragment> getFragmentList() {

        List<Fragment> fragmentList = new ArrayList<>();


        MineItemDynamicFragment dynamicFragment = new MineItemDynamicFragment();
        dynamicFragment.setArguments(bundle);

//        MineItemFavoriteFragment favoriteFragment = new MineItemFavoriteFragment();
//        favoriteFragment.setArguments(bundle);

        MineItemFollowFragment followFragment=new MineItemFollowFragment();
        followFragment.setArguments(bundle);

        fragmentList.add(dynamicFragment);
//        fragmentList.add(favoriteFragment);
        fragmentList.add(followFragment);



        return fragmentList;
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
}
