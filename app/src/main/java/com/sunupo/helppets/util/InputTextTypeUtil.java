package com.sunupo.helppets.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputTextTypeUtil {


    /**
     * 全为数字返回true
     * @param str
     * @return
     */
    public static boolean isNumric(String str){
        if (!TextUtils.isEmpty(str)) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            if (!isNum.matches()) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /*** 判断一段字符是否含有中文字符:如果含有中文则返回true,否则返回false*/
    public static boolean isHaveChina(String str){
        for(int i=0; i<str.length(); i++){
            char ss = str.charAt(i);
            boolean s = String.valueOf(ss).matches("[\u4e00-\u9fa5]");
            if(s){
                return true;
            }
        }return false;
    }



}
