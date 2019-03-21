package com.sunupo.helppets.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.sunupo.helppets.Message.MessageFragment;
import com.sunupo.helppets.Mine.MineFragment;
import com.sunupo.helppets.R;
import com.sunupo.helppets.ReleaseDynamic.GetLocalPhotoActivity;
import com.sunupo.helppets.bean.UserInfo;
import com.sunupo.helppets.home.HomeFragment;
import com.sunupo.helppets.test.ImageDownloadActivity;
import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.util.MyApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.sunupo.helppets.util.MyApplication.loginUserInfo;

public class MainActivity extends AppCompatActivity {

    private final static String TAG="MainActivity";
    private ViewPager viewPager;
    private BottomNavigationPageAdapter pageAdapter;
    private MenuItem menuItem;

//    private Handler handler;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_mine:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_messages:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("宠物互助，爱心传递");
//        TODO 获取当前用户的所有信息

        Intent intent=getIntent();
        String uid=intent.getStringExtra("uid");
        String psw=intent.getStringExtra("psw");

//        handler=new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                Gson gson = new Gson();
//                loginUserInfo = gson.fromJson((String)msg.obj, UserInfo.class);
//                Log.d(TAG, "handleMessage: loginUserInfo="+loginUserInfo.toString());
//            }
//        };
//        getUserInfoJson(uid);//需要在初始化handler之后使用，否者在message.sendToTarget()报空指针异常


        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager=findViewById(R.id.mian_viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                menuItem = navigation.getMenu().getItem(i);
                menuItem.setChecked(true);
            }


            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        pageAdapter=new BottomNavigationPageAdapter(getSupportFragmentManager(),getFragmentList());
        viewPager.setAdapter(pageAdapter);
        //让TabLayout与viewpager产生联动,BottomNavigation好像不需要tablayout的
        //        tabLayout.setupWithViewPager(viewPager);
    }
    public List<Fragment> getFragmentList(){
        /**
         * viewpager的fragment列表
         */
        List<Fragment> fragmentList=new ArrayList<>();

        HomeFragment homeFragment =new HomeFragment();
        Bundle bundle=new Bundle();
        bundle.putString("SAVE_PARA","TEST");
        homeFragment.setArguments(bundle);

        MessageFragment messageFragment =new MessageFragment();
        Bundle bundle2=new Bundle();
        bundle.putString("SAVE_PARA","TEST");
        messageFragment.setArguments(bundle2);

        MineFragment mineFragment =new MineFragment();
        Bundle bundle3=new Bundle();
        bundle.putString("SAVE_PARA","TEST");
        mineFragment.setArguments(bundle3);

        fragmentList.add(homeFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(mineFragment);

        return fragmentList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.menu_search:
                Log.d(TAG, "onOptionsItemSelected:menu_search ");
                // TODO: 3/16/2019 发布一条消息测试
                Intent intenta=new Intent(this,ImageDownloadActivity.class);
                startActivity(intenta);
                   break;
            case R.id.menu_release:
                Log.d(TAG, "onOptionsItemSelected:menu_release ");
                Intent intent=new Intent(this,GetLocalPhotoActivity.class);
                startActivity(intent);
                default:
                    break;
        }
        return super.onOptionsItemSelected(item);
    }
    private int postUp(
            int userID
            ,String loginName
            ,String isSend
            ,String contentText
            ,String type1
            ,String type2
            ,String type3
            ,String type4
            ,String type5
            ,String type6
            ,String time)
    {
        Log.d(TAG, "postUp: ");
        try
        {
            URL url = new URL(Constants.httpip+"/releaseDynamic?userId="+"2"
                    +"&loginName="+loginName
                    +"&isSend="+isSend
                    +"&contentText="+contentText
                    +"&type1="+type1
                    +"&type2="+type2
                    +"&type3="+type3
                    +"&type4="+type4
                    +"&type5="+type5
                    +"&type6="+type6
                    +"&time="+time);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            // TODO: 3/16/2019 连接失败
            Log.d(TAG, "postUp: responseCode="+responseCode);
            if(responseCode == 200)
            {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String resCode = "";
                String readLine = null;
                while ((readLine = bReader.readLine()) != null)
                {
                    resCode += readLine;
                }
                if(resCode.equals("1"))//成功
                {
                    return 1;
                }
                else if(resCode.equals("-2"))//禁言
                {
                    return -2;
                }
            }
            else
            {
                return -1;//失败
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private String getUserInfoJson(String uid){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("uid",uid).build();
                    Request request = new Request.Builder().url(Constants.httpip + "/loginUserInfo").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: loginUserInfo= "+responseData);

//                    Message message=Message.obtain(handler,1,2,3,responseData);
//                    message.sendToTarget();


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
        return null;
    }
}
