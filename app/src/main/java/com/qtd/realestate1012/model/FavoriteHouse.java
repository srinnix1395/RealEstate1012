package com.qtd.realestate1012.model;

/**
 * Created by DELL on 9/25/2016.
 */
public class FavoriteHouse extends CompactHouse {
    private String idBoard;

    public FavoriteHouse(CompactHouse house, String idBoard) {
        super(house.getId(), house.getPrice(), house.getDetailAddress(), house.getStreet(), house.getWard(), house.getDistrict(),
                house.getCity(), house.getFirstImage(), house.getLat(), house.getLng(), house.isLiked);
        this.idBoard = idBoard;
    }

    public FavoriteHouse(String id, int price, String detailAddress, String street, String ward, String district, String city, String firstImage, String lat, String lng, boolean isLiked, String idBoard) {
        super(id, price, detailAddress, street, ward, district, city, firstImage, lat, lng, isLiked);
        this.idBoard = idBoard;
    }

    public String getIdBoard() {
        return idBoard;
    }

    public void setIdBoard(String idBoard) {
        this.idBoard = idBoard;
    }
}
