package com.example.focus390;
public class ImageInfo {
    private float position;
    private String thumbnail;
    private String original;
    private String source;
    private String title;
    private String link;
   
    public float getPosition() {
        return position;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getOriginal() {
        return original;
    }

    public String getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public void setPosition(float position) {
        this.position = position;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }
}