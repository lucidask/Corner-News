package com.example.cornernews;

public class AlertInfo {
    int AlertImage;
    String AlertName;
    String AlertDateTime;
    String CountImageAndVideo;

    public AlertInfo(int alertImage, String alertName, String alertDateTime, String countImageAndVideo) {
        AlertImage = alertImage;
        AlertName = alertName;
        AlertDateTime = alertDateTime;
        CountImageAndVideo = countImageAndVideo;
    }

    public int getAlertImage() {
        return AlertImage;
    }

    public void setAlertImage(int alertImage) {
        AlertImage = alertImage;
    }

    public String getAlertName() {
        return AlertName;
    }

    public void setAlertName(String alertName) {
        AlertName = alertName;
    }

    public String getAlertDateTime() {
        return AlertDateTime;
    }

    public void setAlertDateTime(String alertDateTime) {
        AlertDateTime = alertDateTime;
    }

    public String getCountImageAndVideo() {
        return CountImageAndVideo;
    }

    public void setCountImageAndVideo(String countImageAndVideo) {
        CountImageAndVideo = countImageAndVideo;
    }
}
