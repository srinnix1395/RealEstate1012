package com.qtd.realestate1012.model;

/**
 * Created by Dell on 10/8/2016.
 */
public class District {
    private int idDistrict;
    private String districtName;

    public District( int idDistrict,String districtName) {
        this.districtName = districtName;
        this.idDistrict = idDistrict;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getIdDistrict() {
        return idDistrict;
    }

    public void setIdDistrict(int idDistrict) {
        this.idDistrict = idDistrict;
    }

    @Override
    public String toString() {
        return districtName;
    }
}
