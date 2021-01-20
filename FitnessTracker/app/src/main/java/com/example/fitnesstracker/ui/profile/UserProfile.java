package com.example.fitnesstracker.ui.profile;

public class UserProfile extends HomeFragment{
    public String userName;
    public String userAge;
    public String userGender;

    //Default Constructor
    public UserProfile(){

    }
    //Second constructor

    public UserProfile(String userName, String userAge, String userGender){
        this.userName = userName;
        this.userGender = userGender;
        this.userAge = userAge;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }
}

