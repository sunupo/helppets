package com.sunupo.helppets.conversation;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.sunupo.helppets.Login.LoginActivity;
import com.sunupo.helppets.util.MyApplication;

import io.rong.imkit.MainActivity;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link #init(Context)} 之后调用。</p>
 * <p>如果调用此接口遇到连接失败， SDK 会自动启动重连机制进行最多 10 次重连，分别是 1, 2, 4, 8, 16, 32, 64, 128, 256, 512 秒后。
 * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
 * @param token    从服务端获取的用户身份令牌（Token）。
 *@param callback 连接回调。
 *  @return RongIM  客户端核心类的实例。
 *  */
public class IMUtils extends Activity {

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void connect(String token) {

        if (getApplicationInfo().packageName.equals(MyApplication.getProcessName())) {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                /**
                 * Token 错误。可以从下面两点检查
                 * 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {

                }

                /**
                 * 连接融云成功
                 *  @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.d("LoginActivity", "--onSuccess" + userid);
                    startActivity(new Intent(IMUtils.this, MainActivity.class));
                    finish();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }
}

