package com.qtd.realestate1012.model;

import java.util.ArrayList;

/**
 * Created by Dell on 8/7/2016.
 */
public class Board {
    protected String id;
    protected String name;
    protected ArrayList<String> listHouse;
    protected String image;

    public Board(String id, String name, ArrayList<String> listHouse, String image) {
        this.id = id;
        this.name = name;
        this.listHouse = listHouse;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getListHouse() {
        return listHouse;
    }

    public String getImage() {
        return image;
    }
}
