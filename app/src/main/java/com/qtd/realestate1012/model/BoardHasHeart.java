package com.qtd.realestate1012.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by DELL on 8/29/2016.
 */
public class BoardHasHeart extends Board implements Parcelable {
    private boolean liked;

    public BoardHasHeart(String id, String name, ArrayList<String> listHouse, String image, boolean liked) {
        super(id, name, listHouse, image);
        this.liked = liked;
    }

    protected BoardHasHeart(Parcel in) {
        super(in);
        liked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (liked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BoardHasHeart> CREATOR = new Creator<BoardHasHeart>() {
        @Override
        public BoardHasHeart createFromParcel(Parcel in) {
            return new BoardHasHeart(in);
        }

        @Override
        public BoardHasHeart[] newArray(int size) {
            return new BoardHasHeart[size];
        }
    };

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
