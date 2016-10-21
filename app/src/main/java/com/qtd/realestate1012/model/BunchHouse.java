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

    public int size() {
        return arrayList.size();
    }

    public void resetImvHeart(String id, boolean isLiked) {
        for (CompactHouse house : arrayList) {
            if (house.getId().equals(id)) {
                house.setLiked(isLiked);
                break;
            }
        }
    }

    public void clearHeart() {
        for (CompactHouse house : arrayList) {
            house.setLiked(false);
        }
    }

    public void resetImvHeart(ArrayList<String> listIdHouse) {
        for (CompactHouse house : arrayList) {
            if (listIdHouse.contains(house.getId())) {
                house.setLiked(true);
            }
        }
    }

    public void updateImvHeart(ArrayList<String> listId) {
        for (CompactHouse house : arrayList) {
            if (listId.contains(house.getId())) {
                house.setLiked(!house.isLiked);
            }
        }
    }
}
