package com.sunupo.helppets.Mine;

import java.util.ArrayList;

public class ToApplyBean {
    private ArrayList<FromApplyDetailBean> fromApplyDetailBeanArrayList;
    private String message;
    private int code;

    public ArrayList<FromApplyDetailBean> getFromApplyDetailBeanArrayList() {
        return fromApplyDetailBeanArrayList;
    }

    public void setFromApplyDetailBeanArrayList(ArrayList<FromApplyDetailBean> fromApplyDetailBeanArrayList) {
        this.fromApplyDetailBeanArrayList = fromApplyDetailBeanArrayList;
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
