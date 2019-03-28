package com.sunupo.helppets.search;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sunupo.helppets.R;
import com.sunupo.helppets.bean.DynamicBean;
import com.sunupo.helppets.bean.DynamicBeanData;
import com.sunupo.helppets.util.App;
import com.sunupo.helppets.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {


    private String TAG="SearchActivity";
    RecyclerView recyclerView;

//    这些数据理论上应该根据用户输入的内容来填写，
// 我们暂时直接根据用户输入的内容分词，在每一个字段来一次查询，组合查询结果
    String province="" ;
    String city="";
    String type1="";
    String type2="";
    String type3="";
    String createTime="";
    String limitNumFrom="0";
    String limitNumTo="100";

    TextView label1,label2,label3,label4,label5,label6,changeLabel;
//    ArrayList<TextView> textViews=new ArrayList<>(6);

    private ArrayList<DynamicBean> dynamicBeanArrayList=new ArrayList<>();

    Button finishSearch,beginSearch;
    EditText searchText;
    CollectionAdapter adapter;

    Handler handler;

    Random ra =new Random();
    String[] arr={"哈士奇","泰迪","北京","重庆","哺乳类","飞禽","金毛","吉娃娃","比熊","拉布拉多","中华犬","巴哥","柴犬","柯基","雪瑞纳","约克夏","博美","橘猫","波斯猫","短尾猫","狸花猫","乌龟","仓鼠","兔子","鹦鹉","画眉","八哥"};

    private  void allBind(){
        recyclerView=findViewById(R.id.search_recycler_view);
        finishSearch=findViewById(R.id.finish_search);
        beginSearch=findViewById(R.id.begin_search);
        searchText=findViewById(R.id.search_text);

        label1=findViewById(R.id.label_1);
        label2=findViewById(R.id.label_2);
        label3=findViewById(R.id.label_3);
        label4=findViewById(R.id.label_4);
        label5=findViewById(R.id.label_5);
        label6=findViewById(R.id.label_6);
        changeLabel=findViewById(R.id.change_label);
        changeLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLabel();
            }
        });

        label1.setText(arr[ra.nextInt(arr.length)]);
        label2.setText(arr[ra.nextInt(arr.length)]);
        label3.setText(arr[ra.nextInt(arr.length)]);
        label4.setText(arr[ra.nextInt(arr.length)]);
        label5.setText(arr[ra.nextInt(arr.length)]);
        label6.setText(arr[ra.nextInt(arr.length)]);

        adapter=new CollectionAdapter(dynamicBeanArrayList);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }

   private void changeLabel(){
        label1.setText(arr[ra.nextInt(arr.length)]);
        label2.setText(arr[ra.nextInt(arr.length)]);
        label3.setText(arr[ra.nextInt(arr.length)]);
        label4.setText(arr[ra.nextInt(arr.length)]);
        label5.setText(arr[ra.nextInt(arr.length)]);
        label6.setText(arr[ra.nextInt(arr.length)]);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        allBind();//绑定所有组件

//       todo 取得热门标签取得数据

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        if(msg.arg1==1){
                            dynamicBeanArrayList.clear();
                            dynamicBeanArrayList.addAll((ArrayList<DynamicBean>)msg.obj);
                            adapter.notifyDataSetChanged();
                            Log.d(TAG, "handleMessage: adapter.notifyDataSetChanged();");
                        }else{
                            Log.d(TAG, "handleMessage: 没有搜索到内容");
                            Toast.makeText(SearchActivity.this,"没有搜索到相关内容",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        String result=((String)msg.obj);
                        break;

                }            }
        };


        // TODO: 3/28/2019 注意判断搜索内容是否为空 ，直接给输入内容填充到type3字段，然后在后端一次查找
        View.OnClickListener searchListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchText.getText().toString().trim()==""||searchText.getText().toString().trim().equals("")){
                    Toast.makeText(SearchActivity.this,"请输入查询内容",Toast.LENGTH_SHORT).show();
                }else {
                    getLabelContent(province, city, type1, type2, searchText.getText().toString().trim(), createTime, limitNumFrom, limitNumTo
                            , App.loginUserInfo.getLoginName());
                    Toast.makeText(SearchActivity.this,"正在搜索",Toast.LENGTH_SHORT).show();

                }

            }
        };
        beginSearch.setOnClickListener(searchListener);
        finishSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }finally {
                            finish();
                        }
                    }
                }).start();
            }
        });

//        textViews.add(label1); textViews.add(label2); textViews.add(label3); textViews.add(label4); textViews.add(label5); textViews.add(label6);
        label1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText(label1.getText().toString());
            }
        });
        label2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText(label2.getText().toString());
            }
        });
        label3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText(label3.getText().toString());
            }
        });
        label4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText(label4.getText().toString());
            }
        });
        label5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText(label5.getText().toString());
            }
        });
        label6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText(label6.getText().toString());
            }
        });




    }
    private void getLabelContent(final String province ,final String city,final String type1,final String type2
            ,final String type3,final String createTime,final String limitNumFrom,final String limitNumTo,final String loginName){
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
                    Request request = new Request.Builder().url(Constants.httpip + "/getDynamicJsonSearch").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);
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

    private void getHotLabel(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .build();
                    Request request = new Request.Builder().url(Constants.httpip + "/getHotLabel").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    Message message=Message.obtain(handler,2,2,3,responseData);
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
