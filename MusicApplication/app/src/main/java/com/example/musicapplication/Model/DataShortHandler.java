package com.example.musicapplication.Model;

public class DataShortHandler {
    String accName, descript, avtImg, tag, urlVideo;
    String likeTotal, dislikeTotal, comments;

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getAvtImg() {
        return avtImg;
    }

    public void setAvtImg(String avtImg) {
        this.avtImg = avtImg;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getLikeTotal() {
        return likeTotal;
    }

    public void setLikeTotal(String likeTotal) {
        this.likeTotal = likeTotal;
    }

    public String getDislikeTotal() {
        return dislikeTotal;
    }

    public void setDislikeTotal(String dislikeTotal) {
        this.dislikeTotal = dislikeTotal;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public DataShortHandler(String accName, String descript, String avtImg, String tag, String urlVideo, String likeTotal, String dislikeTotal, String comments) {
        this.accName = accName;
        this.descript = descript;
        this.avtImg = avtImg;
        this.tag = tag;
        this.urlVideo = urlVideo;
        this.likeTotal = likeTotal;
        this.dislikeTotal = dislikeTotal;
        this.comments = comments;
    }

    public DataShortHandler() {
        this.accName = "";
        this.descript = "";
        this.avtImg = "";
        this.tag = "";
        this.urlVideo = "";

    }
}
