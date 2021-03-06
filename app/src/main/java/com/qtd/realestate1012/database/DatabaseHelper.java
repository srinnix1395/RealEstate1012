package com.qtd.realestate1012.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.model.BoardHasHeart;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.model.District;
import com.qtd.realestate1012.model.FavoriteHouse;
import com.qtd.realestate1012.model.ItemSavedSearch;
import com.qtd.realestate1012.model.Street;

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
    public static final String TABLE_SAVED_SEARCH = "tblSavedSearch";
    public static final String TABLE_STREET = "tblStreet";
    public static final String TABLE_DISTRICT = "tblDistrict";

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

    public static final String _ID_DISTRICT = "_ID_DISTRICT";

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static volatile DatabaseHelper mInstance;

    public static DatabaseHelper getInstance(Context mContext) {
        if (mInstance == null) {
            synchronized (DatabaseHelper.class) {
                if (mInstance == null) {
                    mInstance = new DatabaseHelper(mContext);
                }
            }
        }
        return mInstance;
    }

    private DatabaseHelper(Context mContext) {
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

                int recv;
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

    public ArrayList<Board> getListBoard() {
        openDatabase();
        ArrayList<Board> arrayList = new ArrayList<>();
        try {
            Cursor cursor = mDatabase.query(TABLE_BOARD, new String[]{_ID, NAME, COUNT_HOUSE, FIRST_IMAGE}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                int countHouse = cursor.getInt(2);
                String firstImage = cursor.getString(3);

                ArrayList<String> arrayListHouse = new ArrayList<>();
                for (int i = 0; i < countHouse; i++) {
                    arrayListHouse.add(String.valueOf(i));
                }
                arrayList.add(new Board(id, name, arrayListHouse, firstImage));
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

            mDatabase.beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put(_ID, board.getId());
            contentValues.put(NAME, board.getName());
            contentValues.put(FIRST_IMAGE, board.getImage());
            contentValues.put(COUNT_HOUSE, board.getListHouse().size());

            mDatabase.insertOrThrow(TABLE_BOARD, null, contentValues);

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

    public ArrayList<CompactHouse> getListFavoriteHouse() {
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
            mDatabase.delete(TABLE_SAVED_SEARCH, null, null);

            mDatabase.setTransactionSuccessful();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            mDatabase.endTransaction();
            closeDatabase();
        }
    }

    public ArrayList<BoardHasHeart> getListBoardHasHeart(String idHouse) {
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

                    ArrayList<String> arrayListHouse = new ArrayList<>();
                    for (int i = 0; i < countHouse; i++) {
                        arrayListHouse.add(String.valueOf(i));
                    }
                    arrayList.add(new BoardHasHeart(id, name, arrayListHouse, firstImage, id.equals(idBoard)));
                }
            } else {
                while (cursorBoard.moveToNext()) {
                    String id = cursorBoard.getString(0);
                    String name = cursorBoard.getString(1);
                    int countHouse = cursorBoard.getInt(2);
                    String firstImage = cursorBoard.getString(3);

                    ArrayList<String> arrayListHouse = new ArrayList<>();
                    for (int i = 0; i < countHouse; i++) {
                        arrayListHouse.add(String.valueOf(i));
                    }
                    arrayList.add(new BoardHasHeart(id, name, arrayListHouse, firstImage, false));
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

    public ArrayList<CompactHouse> getListFavoriteHouse(String idBoard) {
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
                    FIRST_IMAGE,
            }, _ID_BOARD + "='" + idBoard + "'", null, null, null, null);

            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String detail = cursor.getString(1);
                String street = cursor.getString(2);
                String ward = cursor.getString(3);
                String district = cursor.getString(4);
                String city = cursor.getString(5);
                int price = cursor.getInt(6);
                String firstImage = cursor.getString(7);

                arrayList.add(new CompactHouse(id, price, detail, street, ward, district, city, firstImage, "0", "0", true));
            }
            cursor.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return arrayList;
    }

    public void updateListHouseInBoard(BoardHasHeart board, String firstImage, String action) {
        try {
            openDatabase();

            mDatabase.beginTransaction();

            ContentValues contentValues = new ContentValues();
            if (action.equals(ApiConstant.ACTION_ADD)) {
                if (board.getListHouse().size() == 0) {
                    contentValues.put(FIRST_IMAGE, firstImage);
                }
                contentValues.put(COUNT_HOUSE, board.getListHouse().size() + 1);
            } else {
                if (board.getListHouse().size() == 1) {
                    contentValues.put(FIRST_IMAGE, "");
                }
                contentValues.put(COUNT_HOUSE, board.getListHouse().size() - 1);
            }

            mDatabase.update(TABLE_BOARD, contentValues, _ID + "='" + board.getId() + "'", null);

            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public ArrayList<ItemSavedSearch> getListSavedSearch() {
        ArrayList<ItemSavedSearch> arrayList = new ArrayList<>();

        try {
            openDatabase();

            Cursor cursor = mDatabase.query(TABLE_SAVED_SEARCH, new String[]{
                    _ID,
                    ApiConstant.STATUS,
                    ApiConstant.PRICE_FROM,
                    ApiConstant.PRICE_TO,
                    ApiConstant.NUMBER_OF_ROOMS,
                    ApiConstant.AREA_FROM,
                    ApiConstant.AREA_TO,
                    ApiConstant.PROPERTY_TYPE
            }, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String status = cursor.getString(1);
                int priceFrom = cursor.getInt(2);
                int priceTo = cursor.getInt(3);
                int numberOfRooms = cursor.getInt(4);
                int areaFrom = cursor.getInt(5);
                int areaTo = cursor.getInt(6);
                String property = cursor.getString(7);

                arrayList.add(new ItemSavedSearch(id, status, priceFrom, priceTo, numberOfRooms, areaFrom, areaTo, property));
            }
            cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return arrayList;
    }

    public void insertSavedSearch(ItemSavedSearch item) {
        try {
            openDatabase();

            mDatabase.beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put(_ID, item.getId());
            contentValues.put(ApiConstant.STATUS, item.getStatus());
            contentValues.put(ApiConstant.PRICE_FROM, item.getPriceFrom());
            contentValues.put(ApiConstant.PRICE_TO, item.getPriceTo());
            contentValues.put(ApiConstant.NUMBER_OF_ROOMS, item.getNumberOfRooms());
            contentValues.put(ApiConstant.AREA_FROM, item.getAreaFrom());
            contentValues.put(ApiConstant.AREA_TO, item.getAreaTo());
            contentValues.put(ApiConstant.PROPERTY_TYPE, item.getPropertyType());

            mDatabase.insertOrThrow(TABLE_SAVED_SEARCH, null, contentValues);

            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
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

                    mDatabase.insertOrThrow(TABLE_BOARD, null, contentValues);
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
            mDatabase.endTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public void syncDataFavoriteHouse(ArrayList<FavoriteHouse> arrayListServer) {
        ArrayList<String> arrayListLocal = new ArrayList<>();

        try {
            openDatabase();

            Cursor cursor = mDatabase.query(TABLE_HOUSE_FAVORITE, new String[]{_ID}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                arrayListLocal.add(cursor.getString(0));
            }
            cursor.close();

            mDatabase.beginTransaction();

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
            mDatabase.endTransaction();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public void syncDataSavedSearch(ArrayList<ItemSavedSearch> arrayListServer) {
        try {
            openDatabase();

            Cursor cursor = mDatabase.query(TABLE_SAVED_SEARCH, new String[]{_ID}, null, null, null, null, null);
            ArrayList<String> arrayListLocal = new ArrayList<>();
            while (cursor.moveToNext()) {
                arrayListLocal.add(cursor.getString(0));
            }
            cursor.close();

            mDatabase.beginTransaction();

            for (ItemSavedSearch item : arrayListServer) {
                if (!arrayListLocal.contains(item.getId())) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(_ID, item.getId());
                    contentValues.put(ApiConstant.STATUS, item.getStatus());
                    contentValues.put(ApiConstant.PRICE_FROM, item.getPriceFrom());
                    contentValues.put(ApiConstant.PRICE_TO, item.getPriceTo());
                    contentValues.put(ApiConstant.NUMBER_OF_ROOMS, item.getNumberOfRooms());
                    contentValues.put(ApiConstant.AREA_FROM, item.getAreaFrom());
                    contentValues.put(ApiConstant.AREA_TO, item.getAreaTo());
                    contentValues.put(ApiConstant.PROPERTY_TYPE, item.getPropertyType());

                    mDatabase.insertOrThrow(TABLE_SAVED_SEARCH, null, contentValues);
                }
            }

            for (String id : arrayListLocal) {
                boolean severHas = false;
                for (ItemSavedSearch item : arrayListServer) {
                    if (item.getId().equals(id)) {
                        severHas = true;
                        break;
                    }
                }

                if (!severHas) {
                    mDatabase.delete(TABLE_SAVED_SEARCH, _ID + "='" + id + "'", null);
                }
            }


            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public void insertData(ArrayList<Board> boardArrayList, ArrayList<FavoriteHouse> houseArrayList, ArrayList<ItemSavedSearch> searchArrayList) {
        try {
            openDatabase();
            mDatabase.beginTransaction();

            for (Board board : boardArrayList) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(_ID, board.getId());
                contentValues.put(NAME, board.getName());
                contentValues.put(FIRST_IMAGE, board.getImage());
                contentValues.put(COUNT_HOUSE, board.getListHouse().size());

                mDatabase.insertOrThrow(TABLE_BOARD, null, contentValues);
            }

            for (FavoriteHouse house : houseArrayList) {
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

            for (ItemSavedSearch item : searchArrayList) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(_ID, item.getId());
                contentValues.put(ApiConstant.STATUS, item.getStatus());
                contentValues.put(ApiConstant.PRICE_FROM, item.getPriceFrom());
                contentValues.put(ApiConstant.PRICE_TO, item.getPriceTo());
                contentValues.put(ApiConstant.NUMBER_OF_ROOMS, item.getNumberOfRooms());
                contentValues.put(ApiConstant.AREA_FROM, item.getAreaFrom());
                contentValues.put(ApiConstant.AREA_TO, item.getAreaTo());
                contentValues.put(ApiConstant.PROPERTY_TYPE, item.getPropertyType());

                mDatabase.insertOrThrow(TABLE_SAVED_SEARCH, null, contentValues);
            }

            mDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mDatabase.endTransaction();
            closeDatabase();
        }
    }

    public ArrayList<District> getListDistrict() {
        District district;
        ArrayList<District> listDistrict = new ArrayList<>();

        try {
            openDatabase();
            Cursor cursor = mDatabase.query(TABLE_DISTRICT, new String[]{
                    _ID_DISTRICT,
                    NAME
            }, null, null, null, null, null);

            while (cursor.moveToNext()) {
                district = new District(cursor.getInt(0), cursor.getString(1));
                listDistrict.add(district);
                cursor.moveToNext();
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return listDistrict;
    }

    public ArrayList<Street> getListStreet() {
        Street street;
        ArrayList<Street> listStreet = new ArrayList<>();

        try {
            openDatabase();

            Cursor cursor = mDatabase.query(TABLE_STREET, new String[]{
                    _ID,
                    _ID_DISTRICT,
                    NAME
            }, null, null, null, null, null);

            while (cursor.moveToNext()) {
                street = new Street(cursor.getInt(0), cursor.getInt(1), cursor.getString(2));
                listStreet.add(street);
                cursor.moveToNext();
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return listStreet;
    }
}
