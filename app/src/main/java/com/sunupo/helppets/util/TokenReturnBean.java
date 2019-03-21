package com.sunupo.helppets.util;

public class TokenReturnBean {
//    code	Int	返回码，200 为正常。
//token	String	用户 Token，可以保存应用内，长度在 256 字节以内。
//userId	String	用户 Id，与输入的用户 Id 相同。
    private  int code;
    private String token;
    private String userid;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "TokenReturnBean{" +
                "code=" + code +
                ", token='" + token + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }
}
