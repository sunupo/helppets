package com.sunupo.helppets.Message;

import com.sunupo.helppets.bean.UserInfo;
import com.sunupo.helppets.comment.CommentDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moos on 2018/4/20.
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
