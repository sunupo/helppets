package com.sunupo.helppets.bean;

import java.util.ArrayList;

public class DynamicBeanData {
    private ArrayList<DynamicBean> data;

    public DynamicBeanData(ArrayList<DynamicBean> data) {
        this.data = data;
    }

    public ArrayList<DynamicBean> getData() {
        return data;
    }

    public void setData(ArrayList<DynamicBean> data) {
        this.data = data;
    }
}
