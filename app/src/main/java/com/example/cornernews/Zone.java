package com.example.cornernews;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Zone {
    CircleInstance circle;
    String description;
    String zoneCreatedDateTime;

    public Zone() {

    }

    @Override
    public String toString() {
        return "Zone{" +
                "ZoneCreatedDateTime=" + zoneCreatedDateTime +
                ", circle=" + circle +
                ", description='" + description + '\'' +
                '}';
    }

    public Zone(CircleInstance circle, String description) {
        this.circle=circle;
        this.description = description;
    }

    public Zone(CircleInstance circle, String description, String zoneCreatedDateTime) {
        this.circle = circle;
        this.description = description;
        this.zoneCreatedDateTime = zoneCreatedDateTime;
    }

    public ArrayList<Object> getEventDetailFromThisZone(){
        ArrayList<Object> eventDetail = new ArrayList<>();
        eventDetail.add(description);
        eventDetail.add(circle);
        eventDetail.add(zoneCreatedDateTime);
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

    public String getZoneCreatedDateTime() {
        return zoneCreatedDateTime;
    }

    public void setZoneCreatedDateTime(String zoneCreatedDateTime) {
        zoneCreatedDateTime = zoneCreatedDateTime;
    }
}
