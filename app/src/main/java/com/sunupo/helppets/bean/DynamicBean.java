package com.sunupo.helppets.bean;

public class DynamicBean {
    private String loginName;
    private String logo;//头像
    private String province;
    private String city;//城市

    private int userId;//发布用户id
    private int dynamicId;//动态Id
    private String isSend;
    private String content;//动态内容文字
    private String type1;
    private String type2;
    private String type3;
    private int type4;
    private String type5;
    private int type6;
    private int collect;
    private int favorite;
    private int views;
    private String createTime;//发布时间
    private String picture;//动态内容图片

    private String followFlag;//是否被关注
    private String collectFlag;
    private String favoriteFlag;
    private int successCode;

    public DynamicBean(String loginName, String logo, String province, String city, int userId, int dynamicId, String isSend, String content, String type1, String type2, String type3, int type4, String type5, int type6, int collect, int favorite, int views, String createTime, String picture, String followFlag, String collectFlag, String favoriteFlag, int successCode) {
        this.loginName = loginName;
        this.logo = logo;
        this.province = province;
        this.city = city;
        this.userId = userId;
        this.dynamicId = dynamicId;
        this.isSend = isSend;
        this.content = content;
        this.type1 = type1;
        this.type2 = type2;
        this.type3 = type3;
        this.type4 = type4;
        this.type5 = type5;
        this.type6 = type6;
        this.collect = collect;
        this.favorite = favorite;
        this.views = views;
        this.createTime = createTime;
        this.picture = picture;
        this.followFlag = followFlag;
        this.collectFlag = collectFlag;
        this.favoriteFlag = favoriteFlag;
        this.successCode = successCode;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(int dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getIsSend() {
        return isSend;
    }

    public void setIsSend(String isSend) {
        this.isSend = isSend;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
    }

    public int getType4() {
        return type4;
    }

    public void setType4(int type4) {
        this.type4 = type4;
    }

    public String getType5() {
        return type5;
    }

    public void setType5(String type5) {
        this.type5 = type5;
    }

    public int getType6() {
        return type6;
    }

    public void setType6(int type6) {
        this.type6 = type6;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFollowFlag() {
        return followFlag;
    }

    public void setFollowFlag(String followFlag) {
        this.followFlag = followFlag;
    }

    public String getCollectFlag() {
        return collectFlag;
    }

    public void setCollectFlag(String collectFlag) {
        this.collectFlag = collectFlag;
    }

    public String getFavoriteFlag() {
        return favoriteFlag;
    }

    public void setFavoriteFlag(String favoriteFlag) {
        this.favoriteFlag = favoriteFlag;
    }

    public int getSuccessCode() {
        return successCode;
    }

    public void setSuccessCode(int successCode) {
        this.successCode = successCode;
    }
}
