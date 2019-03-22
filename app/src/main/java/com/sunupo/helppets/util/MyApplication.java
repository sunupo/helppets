package com.sunupo.helppets.util;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
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

                /**
                 * 接受消息监听器
                 */
                RongIM.getInstance().setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
                        @Override
                        public boolean onReceived(Message message, int i) {
                                return false;
                        }
                });
                /**
                 * 连接撞他变化监听器
                 */
                RongIM.getInstance().setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
                        @Override
                        public void onChanged(ConnectionStatus connectionStatus) {

                        }
                });

//                RongIM.registerMessageType(RongRedPacketMessage.class);
//                RongIM.registerMessageTemplate(new RongRedPacketMessageProvider());
//                RongIM.registerMessageType(GiftMessage.class);
//                RongIM.registerMessageTemplate(new GiftMessageProvider());
//                RongIM.getInstance().setSendMessageListener(new MySendMessageListener());
//                RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
//                        @Override
//                        public boolean onReceived(Message message, int i) {
//                                return false;
//                        }
//                });
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


    /**
     * 刷新用户缓存数据。
     * @param userInfo 需要更新的用户缓存数据。
     */
    public static void imRefreshUserInfoCache(String userId,String name,String portraitUri){
        RongIM.getInstance().refreshUserInfoCache(new io.rong.imlib.model.UserInfo(userId, name,Uri.parse(portraitUri)));
    }
    /**
     * 设置用户信息的提供者，供 RongIM 调用获取用户名称和头像信息。
     *
     * @param userInfoProvider 用户信息提供者。
     * @param isCacheUserInfo  设置是否由 IMKit 来缓存用户信息。<br>
     *                         如果 App 提供的 UserInfoProvider
     *                         每次都需要通过网络请求用户数据，而不是将用户数据缓存到本地内存，会影响用户信息的加载速度；<br>
     *                         此时最好将本参数设置为 true，由 IMKit 将用户信息缓存到本地内存中。
     * @see UserInfoProvider
     */
    public static  void imSetUserInfoProvider(){
//        方式一
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public io.rong.imlib.model.UserInfo getUserInfo(String userId) {
                // TODO: 3/22/2019 构造一个融云的userinfo                 new UserInfo(userId, name,Uri.parse(portraitUri);
                return new io.rong.imlib.model.UserInfo("userId", "啊明", Uri.parse("http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png"));//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
            }
        }, true);
        /**
         * 设置当前用户信息，
         * @param userInfo 当前用户信息
         */
        //        方式二
        RongIM.getInstance().setCurrentUserInfo(new io.rong.imlib.model.UserInfo("userId", "啊明", Uri.parse("http://o.png")));
    }

    /**
     * 接下来，在 init 之后调用下面方法设置消息携带用户信息。
     * 设置消息体内是否携带用户信息。
     * @param state 是否携带用户信息，true 携带，false 不携带。
     */
    public static void imSetMessageAttachedUserInfo(boolean b){
        RongIM.getInstance().setMessageAttachedUserInfo(b);
    }
    /**
     * 接收方在接收到消息后，SDK 会自动从消息中取出用户信息，并显示到 UI 上。
     */

    public static void inStartPrivateChat(Context contentText,String userId,String title){
        RongIM.getInstance().startPrivateChat(contentText,userId,title);
    }


}
