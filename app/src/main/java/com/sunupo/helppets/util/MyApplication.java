package com.sunupo.helppets.util;

import android.app.Application;
import android.util.Log;

import com.sunupo.helppets.bean.UserInfo;


public class MyApplication extends Application {

        private final String TAG=MyApplication.class.getSimpleName();
        public static UserInfo loginUserInfo;

        @Override
        public void onCreate() {
                super.onCreate();
                Log.d(TAG, "onCreate: ");
        }

        @Override
        public void onTerminate() {
                super.onTerminate();
                Log.d(TAG, "onTerminate: ");
        }

        @Override
        public void onLowMemory() {
                super.onLowMemory();
                Log.d(TAG, "onLowMemory: ");
        }
}
