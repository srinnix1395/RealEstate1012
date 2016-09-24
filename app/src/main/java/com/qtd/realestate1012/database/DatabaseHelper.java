package com.qtd.realestate1012.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.model.BoardHasHeart;
import com.qtd.realestate1012.model.CompactHouse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by DELL on 9/24/2016.
 */
public class DatabaseHelper {
    public static final String PATH = "/data/data/com.qtd.realestate1012/database/";
    public static final String DATABASE = "HOUSIE_DATABASE.sqlite";

    public static final String TABLE_BOARD = "tblBoard";
    public static final String TABLE_HOUSE_FAVORITE = "tblHouseFavorite";

    public static final String _ID = "_ID";
    public static final String NAME = "Name";
    public static final String FIRST_IMAGE = "First_image";
    public static final String COUNT_HOUSE = "Count_house";

    public static final String DETAIL_ADDRESS = "Detail_address";
    public static final String STREET = "Street";
    public static final String WARD = "Ward";
    public static final String DISTRICT = "District";
    public static final String CITY = "City";
    public static final String PRICE = "Price";
    public static final String _ID_BOARD = "_ID_Board";

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static DatabaseHelper mInstance;

    public static DatabaseHelper getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(mContext);
        }
        return mInstance;
    }

    public DatabaseHelper(Context mContext) {
        this.mContext = mContext;
        copyFile();
    }

    private void copyFile() {
        File file = new File(PATH + DATABASE);
        if (!file.exists()) {
            File parent = new File(PATH);
            if (!parent.exists()) {
                parent.mkdirs();
            }

            try {
                file.createNewFile();

                InputStream input = mContext.getAssets().open(DATABASE);
                FileOutputStream output = new FileOutputStream(file);

                int recv = 0;
                byte[] buff = new byte[2048];

                while (true) {
                    recv = input.read(buff);
                    if (recv <= 0) {
                        break;
                    }
                    output.write(buff, 0, recv);
                }
                input.close();
                output.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openDatabase() {
        mDatabase = mContext.openOrCreateDatabase(PATH + DATABASE, Context.MODE_APPEND, null);
    }

    private void closeDatabase() {
        mDatabase.close();
    }

    public ArrayList<Board> getAllBoards() {
        openDatabase();
        ArrayList<Board> arrayList = new ArrayList<>();
        try {
            Cursor cursor = mDatabase.query(TABLE_BOARD, new String[]{_ID, NAME, COUNT_HOUSE, FIRST_IMAGE}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                int countHouse = cursor.getInt(2);
                String firstImage = cursor.getString(3);

                arrayList.add(new Board(id, name, countHouse, firstImage));
            }
            cursor.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeDatabase();
        }
        return arrayList;
    }

    public void insertBoard(Board board) {
        try {
            openDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(_ID, board.getId());
            contentValues.put(NAME, board.getName());
            contentValues.put(FIRST_IMAGE, board.getImage());
            contentValues.put(COUNT_HOUSE, board.getCountHouse());

            mDatabase.insertOrThrow(TABLE_BOARD, null, contentValues);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public void insertMultiplesBoard(ArrayList<Board> arrayList) {
        //// TODO: 9/24/2016 transaction insert
        try {
            openDatabase();

            mDatabase.beginTransaction();

            for (Board board : arrayList) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(_ID, board.getId());
                contentValues.put(NAME, board.getName());
                contentValues.put(FIRST_IMAGE, board.getImage());
                contentValues.put(COUNT_HOUSE, board.getCountHouse());

                mDatabase.insertOrThrow(TABLE_BOARD, null, contentValues);
            }

            mDatabase.endTransaction();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public void deleteBoard(String idBoard) {
        //// TODO: 9/24/2016 xem lại hàm delete và trả về kết quả
        try {
            openDatabase();

            mDatabase.delete(TABLE_BOARD, _ID + " = '" + idBoard + "'", null);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public ArrayList<CompactHouse> getAllCompactHouses() {
        ArrayList<CompactHouse> arrayList = new ArrayList<>();

        try {
            openDatabase();

            Cursor cursor = mDatabase.query(TABLE_HOUSE_FAVORITE, new String[]{
                    _ID,
                    DETAIL_ADDRESS,
                    STREET,
                    WARD,
                    DISTRICT,
                    CITY,
                    PRICE,
                    FIRST_IMAGE
            }, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String detailAddress = cursor.getString(1);
                String street = cursor.getString(2);
                String ward = cursor.getString(3);
                String district = cursor.getString(4);
                String city = cursor.getString(5);
                int price = cursor.getInt(6);
                String image = cursor.getString(7);

                arrayList.add(new CompactHouse(id, price, detailAddress, street, ward, district, city, image, "0", "0", true));
            }
            cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return arrayList;
    }

    public void insertHouseFavorite(CompactHouse house) {
        try {
            openDatabase();

            mDatabase.beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put(_ID, house.getId());
            contentValues.put(DETAIL_ADDRESS, house.getDetailAddress());
            contentValues.put(STREET, house.getStreet());
            contentValues.put(WARD, house.getWard());
            contentValues.put(DISTRICT, house.getDistrict());
            contentValues.put(CITY, house.getCity());
            contentValues.put(PRICE, house.getPrice());
            contentValues.put(FIRST_IMAGE, house.getFirstImage());

            mDatabase.insertOrThrow(TABLE_HOUSE_FAVORITE, null, contentValues);

            mDatabase.endTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public void deleteHouseFavorite(String id) {
        try {
            openDatabase();

            mDatabase.delete(TABLE_HOUSE_FAVORITE, _ID + "='" + id + "'", null);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public void clearUserData() {
        try {
            openDatabase();

            mDatabase.beginTransaction();

            mDatabase.delete(TABLE_BOARD, null, null);
            mDatabase.delete(TABLE_HOUSE_FAVORITE, null, null);

            mDatabase.endTransaction();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public ArrayList<BoardHasHeart> getAllBoardHasHeart(String idHouse) {
        ArrayList<BoardHasHeart> arrayList = new ArrayList<>();
        try {
            openDatabase();

            String idBoard = "";
            Cursor cursorHouse = mDatabase.query(TABLE_HOUSE_FAVORITE, new String[]{_ID_BOARD}, _ID + "='" + idHouse + "'", null, null, null, null);
            if (cursorHouse.getCount() != 0) {
                cursorHouse.moveToNext();
                idBoard = cursorHouse.getString(0);
            }
            cursorHouse.close();

            Cursor cursorBoard = mDatabase.query(TABLE_BOARD, new String[]{_ID, NAME, COUNT_HOUSE, FIRST_IMAGE}, null, null, null, null, null);
            if (!idBoard.equals("")) {
                while (cursorBoard.moveToNext()) {
                    String id = cursorBoard.getString(0);
                    String name = cursorBoard.getString(1);
                    int countHouse = cursorBoard.getInt(2);
                    String firstImage = cursorBoard.getString(3);

                    arrayList.add(new BoardHasHeart(id, name, countHouse, firstImage, id.equals(idBoard)));
                }
            } else {
                while (cursorBoard.moveToNext()) {
                    String id = cursorBoard.getString(0);
                    String name = cursorBoard.getString(1);
                    int countHouse = cursorBoard.getInt(2);
                    String firstImage = cursorBoard.getString(3);

                    arrayList.add(new BoardHasHeart(id, name, countHouse, firstImage, false));
                }
            }
            cursorBoard.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return arrayList;
    }

    public ArrayList<String> getListIdFavoriteHouse() {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            openDatabase();

            Cursor cursor = mDatabase.query(TABLE_HOUSE_FAVORITE, new String[]{_ID}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                arrayList.add(cursor.getString(0));
            }
            cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return arrayList;
    }
}
