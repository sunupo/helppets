package com.sunupo.helppets.Mine.setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sunupo.helppets.Login.ResetAccountActivity;
import com.sunupo.helppets.R;
import com.sunupo.helppets.comment.CommentMainActivity;
import com.sunupo.helppets.util.App;
import com.sunupo.helppets.util.BaseActivity;
import com.sunupo.helppets.util.Constants;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SettingMainActivity extends BaseActivity {

    private final String TAG="SettingMainActivity";

    private LinearLayout changeUserInfoLayout,changePasswordLayout,changeSignLayout,quit;
    BottomSheetDialog dialog;

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_main);
        setTitle("设置");
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        if(msg.arg1==1){
                            getSignName();
                            Toast.makeText(SettingMainActivity.this,"修改签名成功",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SettingMainActivity.this,"修改签名失败",Toast.LENGTH_SHORT).show();

                        }
                        break;
                    case 2:

                        App.signText=((String)(msg.obj));
                        Log.d(TAG, "handleMessage: App.signText"+App.signText);
                        break;
                }
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeUserInfoLayout=findViewById(R.id.change_user_info_layout);
        changePasswordLayout=findViewById(R.id.change_password_layout);
        changeSignLayout=findViewById(R.id.change_sign_layout);
        quit=findViewById(R.id.quit);


        changeUserInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingMainActivity.this,ChangeUserInfoActivity.class);
                startActivity(intent);
            }
        });
        changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingMainActivity.this,ResetAccountActivity.class);
                startActivity(intent);
            }
        });
        changeSignLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 3/27/2019 编写设置签名的界面和activity
                showDialog();
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingMainActivity.this,"正在退出...",Toast.LENGTH_SHORT).show();
                System.exit(0);
            }
        });

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){

            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * func:弹出框，更改签名
     */
    private void showDialog(){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_apply_layout,null);
        final EditText applyText = (EditText) commentView.findViewById(R.id.dialog_apply_et);
        final Button bt_apply = (Button) commentView.findViewById(R.id.dialog_apply_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_apply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String applyContent = applyText.getText().toString().trim();
                if(!TextUtils.isEmpty(applyContent)){

                    //commentOnWork(commentContent);
                    dialog.dismiss();
                    // TODO: 3/16/2019 根据申请内容 ，往数据库写数据，Constants.httpip+"/apply"

                    setSignName(applyContent);

                }else {
                    Toast.makeText(SettingMainActivity.this,"申请内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        applyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_apply.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_apply.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }
    private void setSignName(final String signText){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userId",App.loginUserInfo.getUserId()+"")
                            .add("signText",signText).build();
                    Request request = new Request.Builder().url(Constants.httpip + "/setSignName").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: getSignName= "+responseData);

                    Message message=Message.obtain(handler,1,Integer.parseInt(responseData),3,responseData);
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

    private  void getSignName(){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userId",App.loginUserInfo.getUserId()+"").build();
                    Request request = new Request.Builder().url(Constants.httpip + "/getSignName").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: getSignName= "+responseData);

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
