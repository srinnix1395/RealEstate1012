package com.qtd.realestate1012.model;

import java.util.ArrayList;

/**
 * Created by Dell on 8/7/2016.
 */
public class BunchHouse {
    private ArrayList<CompactHouse> arrayList;
    private String type;

    public BunchHouse() {
        arrayList = new ArrayList<>();
    }

    public void addHouse(CompactHouse compactHouse) {
        arrayList.add(compactHouse);
    }

    public CompactHouse getCompactHouse(int position) {
        return arrayList.get(position);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int size(){
        return arrayList.size();
    }
}
