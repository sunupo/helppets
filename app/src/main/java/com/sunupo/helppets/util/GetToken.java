package com.sunupo.helppets.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.security.MessageDigest;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetToken {

    public static final String TAG="GetToken";

    public static void getUserToken( final String userId,final String name,final String portraitUri,final Handler handler) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                String url = "https://api.cn.ronghub.com/user/getToken.json";

                String App_Key = Constants.APP_KEY;

                String App_Secret = Constants.APP_SECRET;

                String Timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳，从 1970 年 1 月 1 日 0 点 0 分 0 秒开始到现在的秒数。

                String Nonce = String.valueOf(Math.floor(Math.random() * 1000000));//随机数，无长度限制。

                String Signature = sha1(App_Secret + Nonce + Timestamp);//数据签名。

                Log.d(TAG, "run: Signature="+Signature);
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userId",userId)
                            .add("name",name)
                            .add("portraitUri","http://sun95.320.io:47793/laf/HelpPets.bmp").build();//
                    Request request = new Request.Builder()
                            .addHeader("App-Key", App_Key)
                            .addHeader("Timestamp", Timestamp)
                            .addHeader("Nonce", Nonce)
                            .addHeader("Signature", Signature)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url(url).post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: " + responseData);
                    TokenReturnBean tokenReturnBean=new Gson().fromJson(responseData,TokenReturnBean.class);
                    Log.d(TAG, "run: token="+tokenReturnBean.getToken());
                    Message message=Message.obtain(handler,7,2,3,tokenReturnBean);
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
        //SHA1加密//http://www.rongcloud.cn/docs/server.html#通用_API_接口签名规则

    private static String sha1(String data) {

        StringBuffer buf = new StringBuffer();

        try {

            MessageDigest md = MessageDigest.getInstance("SHA1");

            md.update(data.getBytes());

            byte[] bits = md.digest();

            for (int i = 0; i < bits.length; i++) {

                int a = bits[i];

                if (a < 0) a += 256;

                if (a < 16) buf.append("0");

                buf.append(Integer.toHexString(a));

            }

        } catch (Exception e) {
        }
        return buf.toString();
    }
}
