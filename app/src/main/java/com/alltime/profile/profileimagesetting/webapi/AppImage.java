package com.alltime.profile.profileimagesetting.webapi;

public class AppImage {

    private String imagePath;
    private String thumbnailPath;

    public AppImage(){
    }

    public AppImage(String imagePath, String thumbnailPath){
        this.imagePath = imagePath;
        this.thumbnailPath = thumbnailPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}
