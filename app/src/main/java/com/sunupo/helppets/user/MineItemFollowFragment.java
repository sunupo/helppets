package com.sunupo.helppets.user;

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
import com.sunupo.helppets.bean.DynamicBean;
import com.sunupo.helppets.bean.DynamicBeanData;
import com.sunupo.helppets.util.App;
import com.sunupo.helppets.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MineItemFollowFragment extends Fragment {


    private final String TAG="MineItemFollowFragment";

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
    MineFollowAdapter followAdapter;
    Bundle bundle;
    DynamicBean dynamicBean;
    JSONObject jsonObject;
    String[] followUserIds;
    String hasFollow="-1";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 3/24/2019  发送一个请求，得到这条动态对应用户的所有关注的用户的动态getDynamicUserFollowUser();
        Log.d(TAG, "onCreate: "+((DynamicBean)(getArguments().getSerializable("DYNAMIC_BEAN"))).getLoginName());
        bundle=getArguments();
        dynamicBean=(DynamicBean) bundle.getSerializable("DYNAMIC_BEAN");


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what){
                    case 1:
                        if(msg.arg1==1&&hasFollow.equals("1")){//hasFollow="1",说明followUserIds已经被赋值了
                            dynamicBeanArrayList.clear();
                            for(int i=0;i<((ArrayList<DynamicBean>)(msg.obj)).size();i++){
//                                判断这条动态是否被（登录用户点击的）这个用户所关注
                                for (int j = 0; j < followUserIds.length; j++) {
                                    Log.d(TAG, "handleMessage: case 1:followUserIds["+j+"]="+followUserIds[j]);
                                    if(((ArrayList<DynamicBean>)(msg.obj)).get(i).getUserId()==Integer.parseInt(followUserIds[j]+"")){
                                        dynamicBeanArrayList.add(   ((ArrayList<DynamicBean>)(msg.obj)).get(i)  );
                                        break;
                                    }
                                }

                            }
                            if(dynamicBeanArrayList.size()!=0){
                                followAdapter.notifyDataSetChanged();
                            }
                            Log.d(TAG, "handleMessage: squareAdapter.notifyDataSetChanged();");
                        }else{

                            Log.d(TAG, "handleMessage: 没有follow用户");
                        }
                        break;
                    case 66:
                        try {
                            jsonObject=new JSONObject((String)(msg.obj));
                            if(!(jsonObject.getString("data").equals("-1"))){
                                hasFollow="1";
                                int length=jsonObject.getString("data").split("-").length;
                                followUserIds=new String[length];
                                for (int i = 0; i <length ; i++) {
                                    followUserIds[i]=jsonObject.getString("data").split("-")[i];
                                    Log.d(TAG, "handleMessage: followUserIds["+i+"]"+followUserIds[i]);
                                }
                                sendRequestWithHttpURLConnectionHaveParam(province,city,type1,type2,type3,createTime,limitNumFrom,limitNumTo
                                        ,App.loginUserInfo.getLoginName());
                                Log.d(TAG, "handleMessage: sendRequestWithHttpURLConnectionHaveParam");
                            }else{
                                hasFollow="-1";
                            }

                        } catch (JSONException e) {
                            Log.e(TAG, "handleMessage: 用户关注对象的JSON数据解析失败" );
                            e.printStackTrace();
                        }

                        break;
                }
            }
        };

        getDynamicUserFollowUser(dynamicBean.getUserId(),Constants.httpip+"/getDynamicUserFollowUser");//z在请求万关注列表之后，发送message==99，然后在调用sendRequestWithHttpURLConnectionHaveParam

        View view=inflater.inflate(R.layout.fragment_local,container,false);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        recyclerView=view.findViewById(R.id.local_recycler_view);
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(linearLayoutManager);
        followAdapter  =new MineFollowAdapter(dynamicBeanArrayList);
        followAdapter.setHttpRequest(new MineFollowAdapter.HttpRequest() {
            @Override
            public void sendRequest() {
                sendRequestWithHttpURLConnectionHaveParam(province,city,type1,type2,type3,createTime,limitNumFrom,limitNumTo
                        ,App.loginUserInfo.getLoginName());            }
        });
        recyclerView.setAdapter(followAdapter);
        recyclerView.setAdapter(followAdapter);

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

    /**
     * 得到动态用户的关注用户的id
     * @param dynamicUserId
     * @param url
     */
    private void getDynamicUserFollowUser(final int dynamicUserId,final String url){
        Log.d(TAG, "getDynamicUserFollowUser: ");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("dynamicUserId",dynamicUserId+"").build();
                    Request request = new Request.Builder().url(Constants.httpip + "/getDynamicUserFollowUser").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: "+responseData);

                    Message message=Message.obtain(handler,66,66,66,responseData);
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
