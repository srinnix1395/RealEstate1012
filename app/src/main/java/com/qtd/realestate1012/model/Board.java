package com.qtd.realestate1012.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dell on 8/7/2016.
 */
public class Board implements Parcelable {
    protected String id;
    protected String name;
    protected int countHouse;
    protected String image;

    public Board(String id, String name, int countHouse, String image) {
        this.id = id;
        this.name = name;
        this.countHouse = countHouse;
        this.image = image;
    }

    protected Board(Parcel in) {
        id = in.readString();
        name = in.readString();
        countHouse = in.readInt();
        image = in.readString();
    }

    public static final Creator<Board> CREATOR = new Creator<Board>() {
        @Override
        public Board createFromParcel(Parcel in) {
            return new Board(in);
        }

        @Override
        public Board[] newArray(int size) {
            return new Board[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getCountHouse() {
        return countHouse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeInt(countHouse);
        parcel.writeString(image);
    }
}
