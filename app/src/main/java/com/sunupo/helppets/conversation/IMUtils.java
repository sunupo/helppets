package com.sunupo.helppets.conversation;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentManager;

import com.sunupo.helppets.R;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link #init(Context)} 之后调用。</p>
 * <p>如果调用此接口遇到连接失败， SDK 会自动启动重连机制进行最多 10 次重连，分别是 1, 2, 4, 8, 16, 32, 64, 128, 256, 512 秒后。
 * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
 * @param token    从服务端获取的用户身份令牌（Token）。
 *@param callback 连接回调。
 *  @return RongIM  客户端核心类的实例。
 *  */
public class IMUtils extends Activity {

    /**
     * 刷新用户缓存数据。
     * @param userInfo 需要更新的用户缓存数据。
     */
    public static void imRefreshUserInfoCache(String userId,String name,String portraitUri){
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(userId, name,Uri.parse(portraitUri)));
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
            public UserInfo getUserInfo(String userId) {
                // TODO: 3/22/2019 构造一个融云的userinfo                 new UserInfo(userId, name,Uri.parse(portraitUri);
                return new UserInfo("userId", "啊明", Uri.parse("http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png"));//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
            }
        }, true);
        /**
         * 设置当前用户信息，
         * @param userInfo 当前用户信息
         */
        //        方式二
        RongIM.getInstance().setCurrentUserInfo(new UserInfo("userId", "啊明", Uri.parse("http://o.png")));
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

//    卸载这儿只是为了好集中一点学习，使用的时候还是放在Activity吧
    public  void initConversationList(FragmentManager supportFragmentManager) {
//                FragmentManager supportFragmentManager = getSupportFragmentManager();
        ConversationListFragment conversationlistFragment = (ConversationListFragment) supportFragmentManager.findFragmentById(R.id.conversationlist);
        conversationlistFragment.getActivity();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();
        conversationlistFragment.setUri(uri);
    }

}

