package com.example.cornernews;

public class CircleInstance {
    private String username;
    private String circleName;
    private  Rond rond;

    public CircleInstance() {

    }

    public CircleInstance(String username, String circleName, Rond circle) {
        this.username = username;
        this.circleName = circleName;
        this.rond = circle;
    }

    @Override
    public String toString() {
        return "CircleInstance{" +
                "username='" + username + '\'' +
                ", circleName='" + circleName + '\'' +
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

    public String getCirclename() {
        return circleName;
    }

    public void setCirclename(String circleName) {
        this.circleName = circleName;
    }
}
