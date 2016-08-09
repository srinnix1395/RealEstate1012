package com.qtd.realestate1012.model;

/**
 * Created by Dell on 8/7/2016.
 */
public class Board {
    private int id;
    private String name;
    private int count;
    private String image;

    public Board(int id, String name, int count, String image) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public String getImage() {
        return image;
    }
}
