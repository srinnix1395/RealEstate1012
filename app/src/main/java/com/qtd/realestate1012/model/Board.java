package com.qtd.realestate1012.model;

/**
 * Created by Dell on 8/7/2016.
 */
public class Board {
    protected String id;
    protected String name;
    protected int numberOfHouse;
    protected String image;

    public Board(String id, String name, int numberOfHouse, String image) {
        this.id = id;
        this.name = name;
        this.numberOfHouse = numberOfHouse;
        this.image = image;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfHouse() {
        return numberOfHouse;
    }

    public String getImage() {
        return image;
    }
}
