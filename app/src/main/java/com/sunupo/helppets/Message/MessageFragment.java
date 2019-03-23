package com.sunupo.helppets.Message;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.sunupo.helppets.R;
import com.sunupo.helppets.bean.UserInfo;
import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.util.App;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessageFragment extends Fragment {

    private  static final String TAG="MessageFragment";
    RecyclerView recyclerView;
    ArrayList<UserInfo> userInfoList=new ArrayList<>();
    FollowListAdapter adapter;
    FollowListJsonData followListJsonData;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Gson gson = new Gson();
                    if((String)msg.obj!=""&&!((String)msg.obj).equals("")){
                       try {
                               followListJsonData = gson.fromJson((String) msg.obj, FollowListJsonData.class);
                               userInfoList.addAll(followListJsonData.getData());
                               adapter.notifyDataSetChanged();
                               Log.d(TAG, "handleMessage: adapter.notifyDataSetChanged();");
                       }catch(Exception e){
                           Log.d(TAG, "handleMessage: 没有关注一个用户");
                       }
                    }

            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFollowList(App.loginUserInfo.getUserId());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_message,container,false);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        recyclerView=view.findViewById(R.id.follow_list);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new FollowListAdapter(userInfoList);

        recyclerView.setAdapter(adapter);

        return view;
    }
    private void getFollowList(final int loginUserId) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("loginUserId",loginUserId+"").build();
                    Request request = new Request.Builder().url(Constants.httpip + "/loginUserInfoList").post(requestBody).build();
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
