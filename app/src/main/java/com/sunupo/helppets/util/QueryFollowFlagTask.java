package com.sunupo.helppets.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.sunupo.helppets.R;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 传入两个参数，一个地址，一个对应的imageview对象
 * eg：DownLoadImageTask task=new DownLoadImageTask(imageViewItem).execute(url)
 *
 */


public class QueryFollowFlagTask extends AsyncTask<String, Void, String> {
    private final String TAG="QueryFollowFlagTask";
    ImageView bmImage;

    public QueryFollowFlagTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

//    1是查询到已关注，0是查询到未关注
    protected String doInBackground(String... paras) {
//    sendRequestWithHttpURLConnectionFollow(final String loginName ,final int dynamicUserId,final String followFlag,final String actionFlag,final String url) {

        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder().add("loginName",paras[0]+"")
                    .add("dynamicUserId",paras[1]+"").add("followFlag",paras[2]).add("actionFlag",paras[3]).build();//
            Request request = new Request.Builder().url(paras[4]).post(requestBody).build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            Log.d(TAG, "run:  "+responseData);

            return responseData;

        }catch(IOException e){
        }
            return "10";//默认返回0，未关注
    }

    protected void onPostExecute(String result) {
        switch(result){
            case "11"://已关注
                bmImage.setImageAlpha(255);
                bmImage.setImageResource(R.mipmap.ic_launcher_people);
            case "10"://未关注
                bmImage.setImageAlpha(128);
                bmImage.setImageResource(R.mipmap.ic_launcher_person);

        }

    }
}
