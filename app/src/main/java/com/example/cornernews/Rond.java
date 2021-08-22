package com.example.cornernews;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class Rond {
    Double RondLat=0d;
    Double RondLng=0d;
    Double RondRadius=0d;
    Float RondStrokeWidth=0f;
    int RondStrokeColor=0;
    int RondFillColor=0;
    boolean RondClickable=false;

    public Rond() {
    }

    @Override
    public String toString() {
        return "Rond{" +
                "RondLat=" + RondLat +
                ", RondLng=" + RondLng +
                ", RondRadius=" + RondRadius +
                ", RondStrokeWidth=" + RondStrokeWidth +
                ", RondStrokeColor=" + RondStrokeColor +
                ", RondFillColor=" + RondFillColor +
                ", RondClickable=" + RondClickable +
                '}';
    }

    public Rond(Double rondLat, Double rondLng, Double rondRadius, Float rondStrokeWidth, int rondStrokeColor, int rondFillColor, boolean rondClickable) {
        RondLat = rondLat;
        RondLng = rondLng;
        RondRadius = rondRadius;
        RondStrokeWidth = rondStrokeWidth;
        RondStrokeColor = rondStrokeColor;
        RondFillColor = rondFillColor;
        RondClickable = rondClickable;
    }
    public Rond(Circle circle){
        RondLat = circle.getCenter().latitude;
        RondLng = circle.getCenter().longitude;
        RondRadius = circle.getRadius();
        RondStrokeWidth = circle.getStrokeWidth();
        RondStrokeColor = circle.getStrokeColor();
        RondFillColor = circle.getFillColor();
        RondClickable = circle.isClickable();
    }

    public CircleOptions getcircleOptions(){
        CircleOptions circlemOptions= new CircleOptions();
        circlemOptions.center(new LatLng(RondLat,RondLng));
        circlemOptions.radius(RondRadius);
        circlemOptions.strokeWidth(RondStrokeWidth);
        circlemOptions.strokeColor(RondStrokeColor);
        circlemOptions.fillColor(RondFillColor);
        circlemOptions.clickable(RondClickable);
        return circlemOptions;
    }
    public Double getRondLat() {
        return RondLat;
    }

    public Double getRondLng() {
        return RondLng;
    }

    public Double getRondRadius() {
        return RondRadius;
    }

    public Float getRondStrokeWidth() {
        return RondStrokeWidth;
    }

    public int getRondStrokeColor() {
        return RondStrokeColor;
    }

    public int getRondFillColor() {
        return RondFillColor;
    }

    public boolean isRondClickable() {
        return RondClickable;
    }

    public void setRondLat(Double rondLat) {
        RondLat = rondLat;
    }

    public void setRondLng(Double rondLng) {
        RondLng = rondLng;
    }

    public void setRondRadius(Double rondRadius) {
        RondRadius = rondRadius;
    }

    public void setRondStrokeWidth(Float rondStrokeWidth) {
        RondStrokeWidth = rondStrokeWidth;
    }

    public void setRondStrokeColor(int rondStrokeColor) {
        RondStrokeColor = rondStrokeColor;
    }

    public void setRondFillColor(int rondFillColor) {
        RondFillColor = rondFillColor;
    }

    public void setRondClickable(boolean rondClickable) {
        RondClickable = rondClickable;
    }

    public Circle getCircleFromThisRond(Circle circle){
        circle.setCenter(new LatLng(RondLat,RondLng));
        circle.setStrokeWidth(RondStrokeWidth);
        circle.setStrokeColor(RondStrokeColor);
        circle.setFillColor(RondFillColor);
        circle.setClickable(RondClickable);

        return circle;
    }

}
