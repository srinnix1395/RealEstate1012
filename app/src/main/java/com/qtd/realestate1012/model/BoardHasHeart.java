package com.qtd.realestate1012.model;

import java.util.ArrayList;

/**
 * Created by DELL on 8/29/2016.
 */
public class BoardHasHeart extends Board {
    private boolean liked;

    public BoardHasHeart(String id, String name, ArrayList<String> listHouse, String image, boolean liked) {
        super(id, name, listHouse, image);
        this.liked = liked;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
