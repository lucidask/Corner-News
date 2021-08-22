package com.example.cornernews;

import java.util.ArrayList;

public class Login {
    private String email= "";
    private String username= "";

    public Login(){}

    public Login(String email,String username) {
        this.email=email;
        this.username = username;
    }

    public ArrayList<Object> getEventDetailFromThisLogin(){
        ArrayList<Object> loginEventDetail = new ArrayList<>();
        loginEventDetail.add(email);
        loginEventDetail.add(username);
        return loginEventDetail;
    }

    public String getEmail() {return email; }

    public void setEmail(String email) {this.email = email; }

    public String getUsername() {

        return username;
    }

    public void setUsername(String usermname) {
        this.username = usermname;
    }

}
