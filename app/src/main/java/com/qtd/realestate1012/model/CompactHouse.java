package com.qtd.realestate1012.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dell on 8/7/2016.
 */
public class CompactHouse implements Parcelable {
    protected String id;
    protected int price;
    protected String detailAddress;
    protected String street;
    protected String ward;
    protected String district;
    protected String city;
    protected String firstImage;
    protected String lat;
    protected String lng;
    protected boolean isLiked;

    public CompactHouse(String id, int price, String detailAddress, String street, String ward, String district, String city, String firstImage, String lat, String lng, boolean isLiked) {
        this.id = id;
        this.price = price;
        this.detailAddress = detailAddress;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.firstImage = firstImage;
        this.lat = lat;
        this.lng = lng;
        this.isLiked = isLiked;
    }

    protected CompactHouse(Parcel in) {
        id = in.readString();
        price = in.readInt();
        detailAddress = in.readString();
        street = in.readString();
        ward = in.readString();
        district = in.readString();
        city = in.readString();
        firstImage = in.readString();
        lat = in.readString();
        lng = in.readString();
        isLiked = in.readByte() != 0;
    }

    public static final Creator<CompactHouse> CREATOR = new Creator<CompactHouse>() {
        @Override
        public CompactHouse createFromParcel(Parcel in) {
            return new CompactHouse(in);
        }

        @Override
        public CompactHouse[] newArray(int size) {
            return new CompactHouse[size];
        }
    };

    public String getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public String getStreet() {
        return street;
    }

    public String getWard() {
        return ward;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public String getSmallAddress() {
        return detailAddress + ", " + street;
    }

    public String getLargeAddress() {
        return ward + ", " + district + ", " + city;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeInt(price);
        parcel.writeString(detailAddress);
        parcel.writeString(street);
        parcel.writeString(ward);
        parcel.writeString(district);
        parcel.writeString(city);
        parcel.writeString(firstImage);
        parcel.writeString(lat);
        parcel.writeString(lng);
        parcel.writeByte((byte) (isLiked ? 1 : 0));
    }
}
