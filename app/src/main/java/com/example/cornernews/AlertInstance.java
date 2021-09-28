package com.example.cornernews;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlertInstance {
    CircleInstance circle;
    String description;
    String AlertInstanceCreatedDate;
    String AlertInstanceCreatedTime;

    public AlertInstance() {

    }

    @Override
    public String toString() {
        return "AlertInstance{" +
                "circle=" + circle +
                ", description='" + description + '\'' +
                ", AlertInstanceCreatedDate='" + AlertInstanceCreatedDate + '\'' +
                ", AlertInstanceCreatedTime='" + AlertInstanceCreatedTime + '\'' +
                '}';
    }

    public AlertInstance(CircleInstance circle, String description) {
        this.circle=circle;
        this.description = description;
    }

    public AlertInstance(CircleInstance circle, String description, String AlertInstanceCreatedDate, String AlertInstanceCreatedTime) {
        this.circle = circle;
        this.description = description;
        this.AlertInstanceCreatedDate = AlertInstanceCreatedDate;
        this.AlertInstanceCreatedTime = AlertInstanceCreatedTime;
    }

    public ArrayList<Object> getEventDetailFromThisAlertInstance(){
        ArrayList<Object> eventDetail = new ArrayList<>();
        eventDetail.add(description);
        eventDetail.add(circle);
        eventDetail.add(AlertInstanceCreatedDate);
        eventDetail.add(AlertInstanceCreatedTime);
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

    public String getAlertInstanceCreatedDate() {
        return AlertInstanceCreatedDate;
    }

    public void setAlertInstanceCreatedDate(String AlertInstanceCreatedDate) {
        this.AlertInstanceCreatedDate = AlertInstanceCreatedDate;
    }

    public String getAlertInstanceCreatedTime() {
        return AlertInstanceCreatedTime;
    }

    public void setAlertInstanceCreatedTime(String AlertInstanceCreatedTime) {
        this.AlertInstanceCreatedTime = AlertInstanceCreatedTime;
    }
}
