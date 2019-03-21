package com.sunupo.helppets.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunupo.helppets.R;
import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.util.DownloadImageTask;

import java.io.InputStream;

public class ImageDownloadActivity extends Activity
{
    private static String URL_PATH = Constants.httpip+"/image2_21.bmp";

    private TextView mTextView = null;
    private Button mButton = null;
    private ImageView mImageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_download);

        mTextView = (TextView) findViewById(R.id.info);
        mTextView.setText(URL_PATH);
        mButton = (Button) findViewById(R.id.btn);
        mButton.setOnClickListener(mBtnClickListener);
        mImageView = (ImageView) findViewById(R.id.image);

    }

    private OnClickListener mBtnClickListener = new OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // 首先确认网络连接
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();
            if (null != networkInfo && networkInfo.isConnected())
            {
                new DownloadImageTask(mImageView).execute(URL_PATH);
            }
            else
            {
                Toast.makeText(ImageDownloadActivity.this,
                        "No network connection available", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
//    {
//
//        @Override
//        protected Bitmap doInBackground(String... params)
//        {
//            String path = params[0];
//
//            InputStream inputStream = HttpUtils.getInputStream(path);
//
//            // 从输入流得到位图
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//
//            // 将图像存储到SD卡
////            FileUtils.saveToSDCard(bitmap, "TestImage", "android.jpg");
//
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap result)
//        {
//            // 将图像显示出来
//            mImageView.setImageBitmap(result);
//        }
//
//    }

}
