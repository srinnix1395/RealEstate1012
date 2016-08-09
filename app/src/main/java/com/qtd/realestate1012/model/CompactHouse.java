package com.qtd.realestate1012.model;

/**
 * Created by Dell on 8/7/2016.
 */
public class CompactHouse {
    private String id;
    private int price;
    private String address;
    private String image;


    public CompactHouse(String id, int price, String address, String image) {
        this.id = id;
        this.price = price;
        this.address = address;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }
}