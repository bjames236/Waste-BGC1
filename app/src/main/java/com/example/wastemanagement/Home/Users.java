package com.example.wastemanagement.Home;

public class Users {

    private String userId, email, password, profileImage, status, username, mobile;

    public Users(){

    }

    public Users(String userId, String email, String password, String profileImage, String status, String username, String mobile){
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.status = status;
        this.username = username;
        this.mobile = mobile;
    }
    public String getUserId() {return userId;}
    public void setUserId(String userId) {this.userId = userId;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {return username;}

    public void setUserName(String username) {
        this.username = username;
    }

    public String getMobile(){return mobile;}

    public void setMobile(String mobile){this.mobile = mobile;}
}
