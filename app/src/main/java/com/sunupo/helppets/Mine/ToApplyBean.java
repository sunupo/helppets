package com.sunupo.helppets.Mine;

import java.util.ArrayList;

public class ToApplyBean {
    private ArrayList<ToApplyDetailBean> data;
    private String message;
    private int code;

    public ArrayList<ToApplyDetailBean> getData() {
        return data;
    }

    public void setData(ArrayList<ToApplyDetailBean> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
