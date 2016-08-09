package com.qtd.realestate1012.model;

/**
 * Created by Dell on 8/4/2016.
 */
public class Place {
    private String placeID;
    private double latitude;
    private double longitude;

    public Place(String placeID, double latitude, double longitude) {
        this.placeID = placeID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPlaceID() {
        return placeID;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
