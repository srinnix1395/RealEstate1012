package com.qtd.realestate1012.model;

/**
 * Created by Dell on 8/7/2016.
 */
public class CompactHouse {
    protected String id;
    protected int price;
    protected String detailAddress;
    protected String street;
    protected String ward;
    protected String district;
    protected String city;
    protected String firstImage;


    public CompactHouse(String id, int price, String detailAddress, String street, String ward, String district, String city, String firstImage) {
        this.id = id;
        this.price = price;
        this.detailAddress = detailAddress;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.firstImage = firstImage;
    }

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
}
