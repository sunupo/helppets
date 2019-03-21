package com.sunupo.helppets.util;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.sunupo.helppets.bean.UserInfo;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;


public class MyApplication extends Application {

        private final String TAG=MyApplication.class.getSimpleName();
        public static UserInfo loginUserInfo;
        private static Context mContext;


        @Override
        public void onCreate() {
                super.onCreate();
                RongIM.init(this,Constants.APP_KEY);
//                RongIM.registerMessageType(RongRedPacketMessage.class);
//                RongIM.registerMessageTemplate(new RongRedPacketMessageProvider());
//                RongIM.registerMessageType(GiftMessage.class);
//                RongIM.registerMessageTemplate(new GiftMessageProvider());
//                RongIM.getInstance().setSendMessageListener(new MySendMessageListener());
                RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
                        @Override
                        public boolean onReceived(Message message, int i) {
                                return false;
                        }
                });
//                RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());
//                List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
//                IExtensionModule defaultModule = null;
//                if (moduleList != null) {
//                        for (IExtensionModule module : moduleList) {
//                                if (module instanceof DefaultExtensionModule) {
//                                        defaultModule = module;
//                                        break;
//                                }
//                        }
//                        if (defaultModule != null) {
//                                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
//                                RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModule());
//                        }
//                }
//                RedeceTimeSchedule redeceTimeSchedule = new RedeceTimeSchedule();
//                redeceTimeSchedule.start();
                mContext = getApplicationContext();
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
