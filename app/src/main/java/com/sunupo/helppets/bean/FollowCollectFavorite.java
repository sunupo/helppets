package com.sunupo.helppets.bean;

public class FollowCollectFavorite {
    private String followFlag;
    private String collectFlag;
    private String favoriteFlag;
    private int collectNum;
    private int favoriteNum;
    private int views;

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

    public int getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public int getFavoriteNum() {
        return favoriteNum;
    }

    public void setFavoriteNum(int favoriteNum) {
        this.favoriteNum = favoriteNum;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return "FollowCollectFavorite{" +
                "followFlag='" + followFlag + '\'' +
                ", collectFlag='" + collectFlag + '\'' +
                ", favoriteFlag='" + favoriteFlag + '\'' +
                ", collectNum=" + collectNum +
                ", favoriteNum=" + favoriteNum +
                ", views=" + views +
                '}';
    }
}
