package com.qtd.realestate1012.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Dell on 8/7/2016.
 */
public class Board implements Parcelable{
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

    protected Board(Parcel in) {
        id = in.readString();
        name = in.readString();
        listHouse = in.createStringArrayList();
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

    public ArrayList<String> getListHouse() {
        return listHouse;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeStringList(listHouse);
        parcel.writeString(image);
    }
}
