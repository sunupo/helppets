package com.sunupo.helppets.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//参考https://www.cnblogs.com/mengdd/p/3144599.html
public class HttpUtils
{

//    Java程序：Http连接 获取并下载服务器端图片
//　　写一个工具类：
//　　其中第一个方法根据给出的服务器地址及资源路径得到输入流：
    public static InputStream getInputStream(String path)
    {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try
        {
            URL url = new URL(path);
            if (null != url)
            {
                httpURLConnection = (HttpURLConnection) url.openConnection();

                // 设置连接网络的超时时间
                httpURLConnection.setConnectTimeout(5000);

                // 打开输入流
                httpURLConnection.setDoInput(true);

                // 设置本次Http请求使用的方法
                httpURLConnection.setRequestMethod("GET");

                if (200 == httpURLConnection.getResponseCode())
                {
                    // 从服务器获得一个输入流
                    inputStream = httpURLConnection.getInputStream();

                }

            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return inputStream;
    }

//    第二个方法根据输入流将文件存储在本地一个路径：
    public static void saveInputStream(InputStream inputStream,
            String saveToPath)
    {

        byte[] data = new byte[1024];
        int len = 0;

        FileOutputStream fileOutputStream = null;
        try
        {

            fileOutputStream = new FileOutputStream(saveToPath);
            while (-1 != (len = inputStream.read(data)))
            {

                fileOutputStream.write(data, 0, len);

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != inputStream)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            if (null != fileOutputStream)
            {

                try
                {
                    fileOutputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    //筛选获得动态dynamic的数据
    public static void sendRequestWithHttpURLConnectionHaveParam(final String relation,final String city,final String start,final String end) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder().add("relation",relation).add("city",city).
                            add("start",start).add("end",end).build();
                    Request request = new Request.Builder().url(Constants.httpip + "/getDynamicJson").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
//                    parseJSONWithJSONObject(responseData);
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

//    public void parseJSONWithJSONObject(String responseData){
//        try {
//            list.clear();
//            JSONObject jSONObject = new JSONObject(responseData).getJSONObject("data");
//            JSONArray jsonArray = jSONObject.getJSONArray("list");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONArray curArray = jsonArray.getJSONArray(i);
//                if(curArray.length() == 6){
//                    NineBean ninebean = new NineBean();
//                    ninebean.setAccountid6(curArray.getJSONObject(0).getString("accountId"));
//                    ninebean.setCity6(curArray.getJSONObject(0).getString("city"));
//                    ninebean.setImageUrl6(curArray.getJSONObject(0).getString("profile"));
//                    ninebean.setRelation6(curArray.getJSONObject(0).getString("relation"));
//
//                    ninebean.setAccountid1(curArray.getJSONObject(1).getString("accountId"));
//                    ninebean.setName1(curArray.getJSONObject(1).getString("name"));
//                    ninebean.setCity1(curArray.getJSONObject(1).getString("city"));
//                    ninebean.setImageUrl1(curArray.getJSONObject(1).getString("profile"));
//                    ninebean.setRelation1(curArray.getJSONObject(1).getString("relation"));
//
//                    list.add(ninebean);
//                }
//
//            }
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}