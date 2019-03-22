package com.sunupo.helppets.Message;

import com.sunupo.helppets.bean.UserInfo;
import com.sunupo.helppets.comment.CommentDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunupo
 *
 */

/**
 * 这个是MessageFragment缝纫数据，需要判断是否为空，不然会报异常
 */

public class FollowListJsonData {
    private int code;
    private String message;
    private ArrayList<UserInfo> data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setData(ArrayList<UserInfo> data) {
        this.data = data;
    }
    public ArrayList<UserInfo> getData() {
        return data;
    }

}
