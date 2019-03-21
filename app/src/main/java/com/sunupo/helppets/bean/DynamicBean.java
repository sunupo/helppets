package com.sunupo.helppets.bean;

public class DynamicBean {
    private int userId;//发布用户id
    private String loginName;
    private int dynamicId;//动态Id
//    private int portrait;//头像
    private String createTime;//发布时间
    private String province;
    private String city;//城市
    private String content;//动态内容文字
    private String picture;//动态内容图片
    private String type3;
    private int collect;
    private int favorite;
    private int views;

    private String followFlag;
    private String collectFlag;
    private String favoriteFlag;

    private int loginUserId;//指的是当前登录app用户的id

    public DynamicBean(int userId, String loginName, int dynamicId, String createTime, String province, String city, String content, String picture, String type3, int collect, int favorite, int views, String followFlag, String collectFlag, String favoriteFlag,int loginUserId) {
        this.userId = userId;
        this.loginName = loginName;
        this.dynamicId = dynamicId;
        this.createTime = createTime;
        this.province = province;
        this.city = city;
        this.content = content;
        this.picture = picture;
        this.type3 = type3;
        this.collect = collect;
        this.favorite = favorite;
        this.views = views;
        this.followFlag = followFlag;
        this.collectFlag = collectFlag;
        this.favoriteFlag = favoriteFlag;
        this.loginUserId=loginUserId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public int getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(int dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
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

    public int getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(int loginUserId) {
        this.loginUserId = loginUserId;
    }
}
