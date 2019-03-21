package com.sunupo.helppets.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import com.sunupo.helppets.conversation.IMUtils;
import com.sunupo.helppets.conversation.SubConversationListActivtiy;
import com.sunupo.helppets.home.CollectionAdapter;
import com.sunupo.helppets.home.HomeFragment;
import com.sunupo.helppets.test.ImageDownloadActivity;
import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.util.GetToken;
import com.sunupo.helppets.util.MyApplication;
import com.sunupo.helppets.util.TokenReturnBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.sunupo.helppets.util.GetToken.getUserToken;
import static com.sunupo.helppets.util.MyApplication.loginUserInfo;

public class MainActivity extends AppCompatActivity {

    private final static String TAG="MainActivity";
    private ViewPager viewPager;
    private BottomNavigationPageAdapter pageAdapter;
    private MenuItem menuItem;

    private final Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    connect(((TokenReturnBean)msg.obj).getToken());
            }
        }
    };
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

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.menu_search:
                Log.d(TAG, "onOptionsItemSelected:menu_search ");
//                // TODO: 3/16/2019 发布一条消息测试
//                Intent intenta=new Intent(this,ImageDownloadActivity.class);
//                startActivity(intenta);
                connect(Constants.zhangsanToken);
//                RongIM.getInstance().startSubConversationList(MainActivity.this,Conversation.ConversationType.PRIVATE);
                Map<String, Boolean> supportedConversation=new HashMap<>();
                supportedConversation.put(Conversation.ConversationType.PRIVATE.getName(),false);
                RongIM.getInstance().startConversationList(MainActivity.this,supportedConversation);

//                GetToken.getUserToken("2","zhangsan","",handler);
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


    @RequiresApi(api = Build.VERSION_CODES.P)
    private void connect(String token) {

        Log.d(TAG, "connect: ");
        Log.d(TAG, "connect: "+getApplicationInfo().packageName.equals(MyApplication.getProcessName()));
//
        if (getApplicationInfo().packageName.equals(MyApplication.getProcessName())) {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查
                 * 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    Log.d(TAG, "onTokenIncorrect: ");
                }

                /**
                 * 连接融云成功
                 *  @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.d(TAG, "--onSuccess" + userid);
                    startActivity(new Intent(MainActivity.this, io.rong.imkit.MainActivity.class));
                    finish();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d(TAG, "onError: "+errorCode);
                }
            });
        }
    }
    /**
     * <p>启动会话界面。</p>
     * <p>使用时，可以传入多种会话类型 {@link io.rong.imlib.model.Conversation.ConversationType} 对应不同的会话类型，开启不同的会话界面。
     * 如果传入的是 {@link io.rong.imlib.model.Conversation.ConversationType#CHATROOM}，sdk 会默认调用
     * {@link RongIMClient#joinChatRoom(String, int, RongIMClient.OperationCallback)} 加入聊天室。
     * 如果你的逻辑是，只允许加入已存在的聊天室，请使用接口 {@link #startChatRoomChat(Context, String, boolean)} 并且第三个参数为 true</p>
     *
     * @param context          应用上下文。
     * @param conversationType 会话类型。
     * @param targetId         根据不同的 conversationType，可能是用户 Id、群组 Id 或聊天室 Id。
     * @param title            聊天的标题，开发者可以在聊天界面通过 intent.getData().getQueryParameter("title") 获取该值, 再手动设置为标题。
     */
//    public void startConversation(Context context, Conversation.ConversationType conversationType, String targetId, String title)

    /**
     * 启动会话列表界面。
     *
     * @param context               应用上下文。
     * @param supportedConversation 定义会话列表支持显示的会话类型，及对应的会话类型是否聚合显示。
     *                              例如：supportedConversation.put(Conversation.ConversationType.PRIVATE.getName(), false) 非聚合式显示 private 类型的会话。
     */
//    public void startConversationList(Context context, Map<String, Boolean> supportedConversation)

    /**
     * 启动聚合后的某类型的会话列表。<br> 例如：如果设置了单聊会话为聚合，则通过该方法可以打开包含所有的单聊会话的列表。
     *
     * @param context          应用上下文。
     * @param conversationType 会话类型。
     */
//    public void startSubConversationList(Context context, Conversation.ConversationType conversationType)
}
