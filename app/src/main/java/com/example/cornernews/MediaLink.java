package com.example.cornernews;

public class MediaLink {
    private String mediaCircleName;
    private String linkToMedia;

    public MediaLink(){
    }

    public MediaLink(String mediaCircleName, String linkToMedia) {
        this.mediaCircleName = mediaCircleName;
        this.linkToMedia = linkToMedia;
    }

    public String getMediaCircleName() {
        return mediaCircleName;
    }

    public String getLinkToMedia() {
        return linkToMedia;
    }

    public void setMediaCircleName(String mediaCircleName) {
        this.mediaCircleName = mediaCircleName;
    }

    public void setLinkToMedia(String linkToMedia) {
        this.linkToMedia = linkToMedia;
    }
}
