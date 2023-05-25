package com.ithublive.ktclife.achieversgallery;

public class GridItemModel {
    private String id;
    private String image;
    private String title;

    public GridItemModel() {
        super();
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId(){ return id; }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}