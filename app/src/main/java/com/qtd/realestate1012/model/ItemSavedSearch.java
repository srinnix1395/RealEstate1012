package com.qtd.realestate1012.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 9/28/2016.
 */
public class ItemSavedSearch implements Parcelable {
    private String id;
    private String status;
    private int priceFrom;
    private int priceTo;
    private int numberOfRooms;
    private int areaFrom;
    private int areaTo;
    private String propertyType;


    public ItemSavedSearch(String id, String status, int priceFrom, int priceTo, int numberOfRooms, int areaFrom, int areaTo, String propertyType) {
        this.id = id;
        this.status = status;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.numberOfRooms = numberOfRooms;
        this.areaFrom = areaFrom;
        this.areaTo = areaTo;
        this.propertyType = propertyType;
    }

    protected ItemSavedSearch(Parcel in) {
        id = in.readString();
        status = in.readString();
        priceFrom = in.readInt();
        priceTo = in.readInt();
        numberOfRooms = in.readInt();
        areaFrom = in.readInt();
        areaTo = in.readInt();
        propertyType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(status);
        dest.writeInt(priceFrom);
        dest.writeInt(priceTo);
        dest.writeInt(numberOfRooms);
        dest.writeInt(areaFrom);
        dest.writeInt(areaTo);
        dest.writeString(propertyType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemSavedSearch> CREATOR = new Creator<ItemSavedSearch>() {
        @Override
        public ItemSavedSearch createFromParcel(Parcel in) {
            return new ItemSavedSearch(in);
        }

        @Override
        public ItemSavedSearch[] newArray(int size) {
            return new ItemSavedSearch[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public int getPriceFrom() {
        return priceFrom;
    }

    public int getPriceTo() {
        return priceTo;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public int getAreaFrom() {
        return areaFrom;
    }

    public int getAreaTo() {
        return areaTo;
    }

    public String getPropertyType() {
        return propertyType;
    }
}
