package com.sunupo.helppets.conversation;

import android.content.Context;

import io.rong.push.PushType;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

class SealNotificationReceiver extends PushMessageReceiver {

    @Override
    public boolean onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        return false;//  返回  false,  会弹出融云  SDK  默认通知;  返回  true,  融云 SDK  不会弹通知,  通知需要由您自定义。
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        return false;//  返回  false,  会走融云  SDK  默认处理逻辑,  即点击该通知会打开会话列表或会话界面;  返回  true,  则由您自定义处理逻辑。
    }

    @Override
    public void onThirdPartyPushState(PushType pushType, String action, long resultCode) {
//第三方 push 状态回调,可以带回错误码
        super.onThirdPartyPushState(pushType, action, resultCode);
    }
}