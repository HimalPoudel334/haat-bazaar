package com.example.testapp.basetypes;

public class Location {
    private String District;
    private String City;
    private String Municipality;
    private int wardNo;
    private String toleName;

    public Location(String district, String city, String municipality, int wardNo, String toleName) {
        District = district;
        City = city;
        Municipality = municipality;
        this.wardNo = wardNo;
        this.toleName = toleName;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getMunicipality() {
        return Municipality;
    }

    public void setMunicipality(String municipality) {
        Municipality = municipality;
    }

    public int getWardNo() {
        return wardNo;
    }

    public void setWardNo(int wardNo) {
        this.wardNo = wardNo;
    }

    public String getToleName() {
        return toleName;
    }

    public void setToleName(String toleName) {
        this.toleName = toleName;
    }

}
