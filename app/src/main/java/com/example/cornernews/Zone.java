package com.example.cornernews;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Zone {
    CircleInstance circle;
    String description;
    String zoneCreatedDate;
    String zoneCreatedTime;

    public Zone() {

    }

    @Override
    public String toString() {
        return "Zone{" +
                "circle=" + circle +
                ", description='" + description + '\'' +
                ", zoneCreatedDate='" + zoneCreatedDate + '\'' +
                ", zoneCreatedTime='" + zoneCreatedTime + '\'' +
                '}';
    }

    public Zone(CircleInstance circle, String description) {
        this.circle=circle;
        this.description = description;
    }

    public Zone(CircleInstance circle, String description, String zoneCreatedDate, String zoneCreatedTime) {
        this.circle = circle;
        this.description = description;
        this.zoneCreatedDate = zoneCreatedDate;
        this.zoneCreatedTime = zoneCreatedTime;
    }

    public ArrayList<Object> getEventDetailFromThisZone(){
        ArrayList<Object> eventDetail = new ArrayList<>();
        eventDetail.add(description);
        eventDetail.add(circle);
        eventDetail.add(zoneCreatedDate);
        eventDetail.add(zoneCreatedTime);
        return eventDetail;
    }


    public CircleInstance getCircle() {
        return circle;
    }

    public String getDescription() {
        return description;
    }

    public void setCircle(CircleInstance circle) {
        this.circle = circle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getZoneCreatedDate() {
        return zoneCreatedDate;
    }

    public void setZoneCreatedDate(String zoneCreatedDate) {
        this.zoneCreatedDate = zoneCreatedDate;
    }

    public String getZoneCreatedTime() {
        return zoneCreatedTime;
    }

    public void setZoneCreatedTime(String zoneCreatedTime) {
        this.zoneCreatedTime = zoneCreatedTime;
    }
}
