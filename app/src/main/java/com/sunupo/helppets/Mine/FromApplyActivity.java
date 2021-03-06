package com.sunupo.helppets.Mine;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sunupo.helppets.R;
import com.sunupo.helppets.util.App;
import com.sunupo.helppets.util.Constants;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FromApplyActivity extends AppCompatActivity {

    private final String TAG="FromApplyActivity";
    FromApplyAdapter applyAdapter;
    FromApplyBean fromApplyBean;
    RecyclerView recyclerView;
    ArrayList<FromApplyDetailBean> fromApplyDetailBeanArrayList=new ArrayList<>();
    Handler handler;

    TextView warningInfo;
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_apply);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        try{
                            if((msg.obj)!=null){
                                fromApplyDetailBeanArrayList.clear();
                                Gson gson=new Gson();
                                fromApplyBean=gson.fromJson((String)(msg.obj),FromApplyBean.class);
                                fromApplyDetailBeanArrayList.addAll(fromApplyBean.getData());
                                Log.d(TAG, "handleMessage: fromApplyDetailBeanArrayList.size="+fromApplyDetailBeanArrayList.size());

                                applyAdapter.notifyDataSetChanged();

//                                Log.d(TAG, "handleMessage: applyAdapter.notifyDataSetChanged();");
//                                recyclerView=findViewById(R.id.from_apply_recycler_view);
//
//                                LinearLayoutManager manager=new LinearLayoutManager(FromApplyActivity.this);
//                                recyclerView.setLayoutManager(manager);
//
//                                applyAdapter=new FromApplyAdapter(fromApplyDetailBeanArrayList);
//                                recyclerView.setAdapter(applyAdapter);
                            }
                            else{
                                Toast.makeText(FromApplyActivity.this,"没有申请",Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "else: 没有申请列表");
                                warningInfo.setVisibility(View.VISIBLE);
                            }

                        }catch(Exception e){
                            Toast.makeText(FromApplyActivity.this,"没有申请 ",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Exception: 没有申请列表");
                            warningInfo.setVisibility(View.VISIBLE);

                        }
                        
                        break;
                }

            }
        };

        warningInfo=findViewById(R.id.warning_info);
        warningInfo.setVisibility(View.GONE);

        recyclerView=findViewById(R.id.from_apply_recycler_view);
        LinearLayoutManager manager=new LinearLayoutManager(FromApplyActivity.this);
        recyclerView.setLayoutManager(manager);
        applyAdapter=new FromApplyAdapter(fromApplyDetailBeanArrayList);
        recyclerView.setAdapter(applyAdapter);
        applyAdapter.setHttpRequest(new FromApplyAdapter.HttpRequest() {
            @Override
            public void sendRequest() {
                getFromApply();
            }
        });
        getFromApply();//到登录用户收到的申请列表

    }

    /**
     * 得到登录用户收到的申请列表
     */
    private void getFromApply() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("loginUserId", App.loginUserInfo.getUserId()+"").build();
                    Request request = new Request.Builder().url(Constants.httpip + "/getFromApply").post(requestBody).build();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
