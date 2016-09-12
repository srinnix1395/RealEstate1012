package com.qtd.realestate1012.model;

/**
 * Created by DELL on 9/11/2016.
 */
public class User {
    private String id;
    private String name;
    private String image;
    private String email;
    private String provider;
    private String phoneNumber;
    private boolean noti;

    public User(String id, String name, String image, String email, String accountType, String phoneNumber, boolean noti) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.provider = accountType;
        this.phoneNumber = phoneNumber;
        this.noti = noti;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProvider() {
        return provider;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isNoti() {
        return noti;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNoti(boolean noti) {
        this.noti = noti;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        if (name.equals(user.getName()) && phoneNumber.equals(user.getPhoneNumber()) && noti == user.isNoti()) {
            return true;
        }
        return false;
    }
}
