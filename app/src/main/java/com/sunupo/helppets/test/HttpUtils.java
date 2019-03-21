package com.sunupo.helppets.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils
{

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

}
