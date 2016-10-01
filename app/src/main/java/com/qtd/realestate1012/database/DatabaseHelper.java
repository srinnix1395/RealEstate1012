package com.qtd.realestate1012.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.model.BoardHasHeart;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.model.FavoriteHouse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by DELL on 9/24/2016.
 */
public class DatabaseHelper {
    public static final String PATH = "/data/data/com.qtd.realestate1012/databases/";
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

                arrayList.add(new Board(id, name, new ArrayList<String>(countHouse), firstImage));
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
            contentValues.put(COUNT_HOUSE, board.getListHouse().size());

            mDatabase.insertOrThrow(TABLE_BOARD, null, contentValues);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public void insertMultiplesBoard(ArrayList<Board> arrayList) {
        try {
            openDatabase();

            mDatabase.beginTransaction();

            for (Board board : arrayList) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(_ID, board.getId());
                contentValues.put(NAME, board.getName());
                contentValues.put(FIRST_IMAGE, board.getImage());
                contentValues.put(COUNT_HOUSE, board.getListHouse().size());

                mDatabase.insert(TABLE_BOARD, null, contentValues);
            }

            mDatabase.setTransactionSuccessful();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            mDatabase.endTransaction();
            closeDatabase();
        }
    }

    public boolean deleteBoard(String idBoard) {
        try {
            openDatabase();

            int result = mDatabase.delete(TABLE_BOARD, _ID + " = '" + idBoard + "'", null);
            if (result == 1) {
                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeDatabase();
        }
        return false;
    }

    public ArrayList<CompactHouse> getAllFavoriteHouse() {
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

    public void insertHouseFavorite(FavoriteHouse house) {
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
            contentValues.put(_ID_BOARD, house.getIdBoard());

            mDatabase.insertOrThrow(TABLE_HOUSE_FAVORITE, null, contentValues);

            mDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mDatabase.endTransaction();
            closeDatabase();
        }
    }

    public boolean deleteHouseFavorite(String id) {
        try {
            openDatabase();

            int result = mDatabase.delete(TABLE_HOUSE_FAVORITE, _ID + "='" + id + "'", null);
            if (result == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return false;
    }

    public void clearUserData() {
        try {
            openDatabase();

            mDatabase.beginTransaction();

            mDatabase.delete(TABLE_BOARD, null, null);
            mDatabase.delete(TABLE_HOUSE_FAVORITE, null, null);

            mDatabase.setTransactionSuccessful();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            mDatabase.endTransaction();
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

                    arrayList.add(new BoardHasHeart(id, name, new ArrayList<String>(countHouse), firstImage, id.equals(idBoard)));
                }
            } else {
                while (cursorBoard.moveToNext()) {
                    String id = cursorBoard.getString(0);
                    String name = cursorBoard.getString(1);
                    int countHouse = cursorBoard.getInt(2);
                    String firstImage = cursorBoard.getString(3);

                    arrayList.add(new BoardHasHeart(id, name, new ArrayList<String>(countHouse), firstImage, false));
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

    public void syncDataBoard(ArrayList<Board> arrayListServer) {
        try {
            openDatabase();

            Cursor cursor = mDatabase.query(TABLE_BOARD, new String[]{_ID}, null, null, null, null, null);
            ArrayList<String> arrayListLocal = new ArrayList<>();
            while (cursor.moveToNext()) {
                arrayListLocal.add(cursor.getString(0));
            }
            cursor.close();

            mDatabase.beginTransaction();

            for (Board board : arrayListServer) {
                if (!arrayListLocal.contains(board.getId())) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(_ID, board.getId());
                    contentValues.put(NAME, board.getName());
                    contentValues.put(COUNT_HOUSE, board.getListHouse().size());
                    contentValues.put(FIRST_IMAGE, board.getImage());

                    mDatabase.insert(TABLE_BOARD, null, contentValues);
                }
            }

            for (String id : arrayListLocal) {
                boolean severHas = false;
                for (Board board : arrayListServer) {
                    if (board.getId().equals(id)) {
                        severHas = true;
                        break;
                    }
                }

                if (!severHas) {
                    mDatabase.delete(TABLE_BOARD, _ID + "='" + id + "'", null);
                }
            }


            mDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mDatabase.endTransaction();
            closeDatabase();
        }
    }

    public void syncDataFavoriteHouse(ArrayList<FavoriteHouse> arrayListServer) {
        // TODO: 9/26/2016 datasync
        ArrayList<String> arrayListLocal = new ArrayList<>();

        try {
            openDatabase();

            Cursor cursor = mDatabase.query(TABLE_HOUSE_FAVORITE, new String[]{_ID}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                arrayListLocal.add(cursor.getString(0));
            }
            cursor.close();

            for (FavoriteHouse house : arrayListServer) {
                if (!arrayListLocal.contains(house.getId())) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(_ID, house.getId());
                    contentValues.put(DETAIL_ADDRESS, house.getDetailAddress());
                    contentValues.put(STREET, house.getStreet());
                    contentValues.put(WARD, house.getWard());
                    contentValues.put(DISTRICT, house.getDistrict());
                    contentValues.put(CITY, house.getCity());
                    contentValues.put(PRICE, house.getPrice());
                    contentValues.put(FIRST_IMAGE, house.getFirstImage());
                    contentValues.put(_ID_BOARD, house.getIdBoard());

                    mDatabase.insertOrThrow(TABLE_HOUSE_FAVORITE, null, contentValues);
                }
            }

            for (String id : arrayListLocal) {
                boolean severHas = false;
                for (FavoriteHouse house : arrayListServer) {
                    if (house.getId().equals(id)) {
                        severHas = true;
                        break;
                    }
                }

                if (!severHas) {
                    mDatabase.delete(TABLE_HOUSE_FAVORITE, _ID + "='" + id + "'", null);
                }
            }

            mDatabase.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mDatabase.endTransaction();
            closeDatabase();
        }
    }
}
