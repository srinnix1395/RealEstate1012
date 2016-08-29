package com.qtd.realestate1012.model;

/**
 * Created by DELL on 8/29/2016.
 */
public class BoardHasHeart extends Board {
    private boolean liked;

    public BoardHasHeart(String id, String name, int numberOfHouse, String image, boolean liked) {
        super(id, name, numberOfHouse, image);
        this.liked = liked;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
