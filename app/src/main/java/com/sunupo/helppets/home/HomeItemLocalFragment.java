package com.sunupo.helppets.home;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.sunupo.helppets.R;
import com.sunupo.helppets.bean.DynamicBean;
import com.sunupo.helppets.bean.DynamicBeanData;
import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.util.MyApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class HomeItemLocalFragment extends Fragment {


    private final String TAG="HomeItemLocalFragment";

    private RecyclerView recyclerView;
    final String province="" ;
    final String city="";
    final String type1="";
    final String type2="";
    final String type3="";
    final String createTime="";
    final String limitNumFrom="0";
    final String limitNumTo="100";
    private  ArrayList<DynamicBean> dynamicBeanArrayList=new ArrayList<>();

    private Handler handler;
    LocalAdapter localAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what){
                    case 1:
                        if(msg.arg1==1){
                            dynamicBeanArrayList.clear();
                            for(int i=0;i<((ArrayList<DynamicBean>)(msg.obj)).size();i++){
                                if(((ArrayList<DynamicBean>)(msg.obj)).get(i).getCity().equals(MyApplication.loginUserInfo.getCity())){
                                    dynamicBeanArrayList.add(   ((ArrayList<DynamicBean>)(msg.obj)).get(i)  );
                                }
                            }
                            localAdapter.notifyDataSetChanged();
                            Log.d(TAG, "handleMessage: squareAdapter.notifyDataSetChanged();");
                        }else{
                            //必须把自己排除在外，不然同城用户全是自己
                            Log.d(TAG, "handleMessage: 没有同城用户");
                        }
                }
            }
        };
        sendRequestWithHttpURLConnectionHaveParam(province,city,type1,type2,type3,createTime,limitNumFrom,limitNumTo
                ,MyApplication.loginUserInfo.getLoginName());
        View view=inflater.inflate(R.layout.fragment_local,container,false);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        recyclerView=view.findViewById(R.id.local_recycler_view);
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(linearLayoutManager);
        localAdapter  =new LocalAdapter(dynamicBeanArrayList);
        localAdapter.setHttpRequest(new LocalAdapter.HttpRequest() {
            @Override
            public void sendRequest() {
                sendRequestWithHttpURLConnectionHaveParam(province,city,type1,type2,type3,createTime,limitNumFrom,limitNumTo
                        ,MyApplication.loginUserInfo.getLoginName());            }
        });
        recyclerView.setAdapter(localAdapter);
        recyclerView.setAdapter(localAdapter);

        return view;
    }

    //筛选获得动态dynamic的数据
//    在创建了handler之后调用
    private void sendRequestWithHttpURLConnectionHaveParam(final String province ,final String city,final String type1,final String type2
            ,final String type3,final String createTime,final String limitNumFrom,final String limitNumTo,final String loginName) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("paraProvince",province).add("paraCity",city).
                                    add("paraType1",type1).add("paraType2",type2)
                            .add("paraType3",type3).add("paraCreateTime",createTime)
                            .add("limitNumFrom",limitNumFrom).add("limitNumTo",limitNumTo)
                            .add("loginName",loginName).build();
                    Request request = new Request.Builder().url(Constants.httpip + "/getDynamicJson").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: "+responseData);
                    parseJSONWithJSONObject(responseData);
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
    }
    public void parseJSONWithJSONObject(String responseData){
        try {
            int successCode=0;
            DynamicBeanData dynamicBeanData;
            Gson gson = new Gson();
            dynamicBeanData = gson.fromJson(responseData, DynamicBeanData.class);
            ArrayList<DynamicBean> dynamicBeanArrayList = dynamicBeanData.getData();

            if(dynamicBeanArrayList.size()==0){
                //啥也不做，因为，列表为0，说明没有数据successCode=0;，最好这儿创建一个message，就return；
                Message message=Message.obtain(handler,1,successCode,3,dynamicBeanArrayList);
                message.sendToTarget();
                return ;
            }else if(dynamicBeanArrayList.get(dynamicBeanArrayList.size()-1).getSuccessCode()==1){
                successCode=1;
                Log.d(TAG, "parseJSONWithJSONObject: "+successCode);
            }
            Message message=Message.obtain(handler,1,successCode,3,dynamicBeanArrayList);
            message.sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
