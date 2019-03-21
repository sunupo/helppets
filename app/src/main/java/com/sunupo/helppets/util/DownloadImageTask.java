package com.sunupo.helppets.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;


/**
 * 传入两个参数，一个地址，一个对应的imageview对象
 * eg：DownLoadImageTask task=new DownLoadImageTask(imageViewItem).execute(url)
 *
 */


public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            mIcon11 = BitmapFactory.decodeStream(in,null, options);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
/*            String path = urls[0];

            InputStream inputStream = HttpUtils.getInputStream(path);

            // 从输入流得到位图
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            return bitmap;*/
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
