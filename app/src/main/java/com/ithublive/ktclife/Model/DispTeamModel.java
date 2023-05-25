package com.ithublive.ktclife.Model;

public class DispTeamModel {

    public String profile_imgurl, name, userid, isPaid;
    public int paidLegsNum;
    public int unPaidLegsNum;
    public String levelColor;

    public DispTeamModel() {
    }

    public DispTeamModel(String profile_imgurl, String name, String userid, String isPaid,int paidLegsNum,int unPaidLegsNum,String levelColor) {
        this.profile_imgurl = profile_imgurl;
        this.name = name;
        this.userid = userid;
        this.isPaid = isPaid;
        this.paidLegsNum=paidLegsNum;
        this.unPaidLegsNum=unPaidLegsNum;
        this.levelColor=levelColor;
    }

    public String getProfile_imgurl() {
        return profile_imgurl;
    }

    public void setProfile_imgurl(String profile_imgurl) {
        this.profile_imgurl = profile_imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public int getPaidLegsNum() {
        return paidLegsNum;
    }

    public void setPaidLegsNum(int paidLegsNum) {
        this.paidLegsNum = paidLegsNum;
    }

    public int getUnPaidLegsNum() {
        return unPaidLegsNum;
    }

    public void setUnPaidLegsNum(int unPaidLegsNum) {
        this.unPaidLegsNum = unPaidLegsNum;
    }

    public String getLevelColor() {
        return levelColor;
    }

    public void setLevelColor(String levelColor) {
        this.levelColor = levelColor;
    }
}
