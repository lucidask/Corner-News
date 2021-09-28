package com.example.cornernews;

public class CircleInstance {
    private String username;
    private String AlertName;
    private  Rond rond;

    public CircleInstance() { }

    public CircleInstance(String username, String AlertName, Rond circle) {
        this.username = username;
        this.AlertName = AlertName;
        this.rond = circle;
    }

    @Override
    public String toString() {
        return "CircleInstance{" +
                "username='" + username + '\'' +
                ", AlertName='" + AlertName + '\'' +
                ", rond=" + rond +
                '}';
    }

    public Rond getRond() {
        return rond;
    }

    public void setRond(Rond rond) {
        this.rond = rond;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlertName() {
        return AlertName;
    }

    public void setAlertName(String AlertName) {
        this.AlertName = AlertName;
    }
}
