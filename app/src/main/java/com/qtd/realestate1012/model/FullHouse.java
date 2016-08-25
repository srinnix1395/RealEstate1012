package com.qtd.realestate1012.model;

import java.util.ArrayList;

/**
 * Created by DELL on 8/25/2016.
 */
public class FullHouse extends CompactHouse {
    private float area;
    private int numberOfRoom;
    private String status;
    private String propertyType;
    private String description;
    private String lat;
    private String lng;
    private String emailOwner;
    private String phoneOwner;
    private ArrayList<String> images;

    public FullHouse(String id, int price, String address, float area, int numberOfRoom, String status, String propertyType, String description, String lat, String lng, String emailOwner, String phoneOwner, ArrayList<String> images) {
        super(id, price, address, images.get(0));
        this.area = area;
        this.numberOfRoom = numberOfRoom;
        this.status = status;
        this.propertyType = propertyType;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.emailOwner = emailOwner;
        this.phoneOwner = phoneOwner;
        this.images = images;
    }

    public float getArea() {
        return area;
    }

    public int getNumberOfRoom() {
        return numberOfRoom;
    }

    public String getStatus() {
        return status;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public String getDescription() {
        return description;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getEmailOwner() {
        return emailOwner;
    }

    public String getPhoneOwner() {
        return phoneOwner;
    }

    public ArrayList<String> getImages() {
        return images;
    }
}
