package com.sunupo.helppets.Mine;

import java.util.ArrayList;

public class FromApplyBean {
    private ArrayList<FromApplyDetailBean> data;
    private String message;
    private int code;

    public ArrayList<FromApplyDetailBean> getData() {
        return data;
    }

    public void setData(ArrayList<FromApplyDetailBean> data) {
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
