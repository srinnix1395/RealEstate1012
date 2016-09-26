package com.qtd.realestate1012.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by DELL on 8/25/2016.
 */
public class FullHouse extends CompactHouse implements Parcelable {
    private int area;
    private int numberOfRoom;
    private String status;
    private String propertyType;
    private String description;
    private String address;
    private ArrayList<String> images;

    public FullHouse(String id, int price, String detailAddress, String street, String ward, String district
            , String city, boolean isLiked, int area, int numberOfRoom, String status, String propertyType, String description
            , String lat, String lng, String address, ArrayList<String> images) {
        super(id, price, detailAddress, street, ward, district, city, images.get(0), lat, lng, isLiked);
        this.area = area;
        this.numberOfRoom = numberOfRoom;
        this.status = status;
        this.propertyType = propertyType;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.images = images;
    }

    protected FullHouse(Parcel in) {
        super(in);
        area = in.readInt();
        numberOfRoom = in.readInt();
        status = in.readString();
        propertyType = in.readString();
        description = in.readString();
        address = in.readString();
        images = in.createStringArrayList();
    }

    public static final Creator<FullHouse> CREATOR = new Creator<FullHouse>() {
        @Override
        public FullHouse createFromParcel(Parcel in) {
            return new FullHouse(in);
        }

        @Override
        public FullHouse[] newArray(int size) {
            return new FullHouse[size];
        }
    };

    public int getArea() {
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

    public String getAddress() {
        return address;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(area);
        parcel.writeInt(numberOfRoom);
        parcel.writeString(status);
        parcel.writeString(propertyType);
        parcel.writeString(description);
        parcel.writeString(address);
        parcel.writeStringList(images);
    }
}
